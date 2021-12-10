package com.dreamz.samtaBhratruMandal.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamz.samtaBhratruMandal.Activities.MainActivity;
import com.dreamz.samtaBhratruMandal.Adapters.SearchAdapter;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.SearchResultDTO;
import com.dreamz.samtaBhratruMandal.Models.SearchResultsItem;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.dreamz.samtaBhratruMandal.Utils.UserSessionManager;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilterFragment extends Fragment {

    int userId;
    UserSessionManager session;
    ShowLoader showLoader;
    List<String> jobLocationList;
    //List<String> hometownList;
    List<String> heightList;
    List<String> hghtList;
    List<String> bloodGroupList;
    List<String> birthYearList;
    List<String> yearList;
    //List<String> qualificationList;
    List<String> educationLevelList;
    List<String> gotraList;
    List<String> professionList;
    List<String> categoryList;
    List<String> varnList;
    List<SearchResultsItem> searchList;
    SearchAdapter adapter;
    String selFname, selMname, selLname, selVarn,
            selCategory, selBirthYear, selBloodGroup, selHeight, selEducation, selEducationLevel, selJobLocation, selGotra, selHometown, selProfession;
    JsonObject obj, basicObj;
    private EditText etFname, etMname, etLname, etEducation, etHometown,etEducationPlace;
    private Spinner spCategory, spBirthYear, spBloodGroup, spHeight, spVarn, spJobLocation, spGotra, spProfession, spEducationLevel;
    private View view;
    private Button btnReset, btnSearch, btnBasicSearch;
    private RecyclerView rvSearch;

    public SearchFilterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        etFname = view.findViewById(R.id.et_fname);
        etMname = view.findViewById(R.id.et_mname);
        etLname = view.findViewById(R.id.et_lname);
        spVarn = view.findViewById(R.id.sp_varn);
        spCategory = view.findViewById(R.id.sp_category);
        spBirthYear = view.findViewById(R.id.sp_byear);
        spBloodGroup = view.findViewById(R.id.sp_bloodGroup);
        spHeight = view.findViewById(R.id.sp_maxHeight);
        spEducationLevel = view.findViewById(R.id.sp_educationLevel);
        etEducation = view.findViewById(R.id.et_education);
        //spJobLocation = view.findViewById(R.id.sp_jobLocation);
        spGotra = view.findViewById(R.id.sp_gotra);
        etHometown = view.findViewById(R.id.et_mulgav);
        spProfession = view.findViewById(R.id.sp_profession);
        btnReset = view.findViewById(R.id.btn_reset);
        btnSearch = view.findViewById(R.id.btn_search);
        btnBasicSearch = view.findViewById(R.id.btn_basicSearch);
        rvSearch = view.findViewById(R.id.rv_search);
        etEducationPlace=view.findViewById(R.id.et_education_place);

        MainActivity.toolbar.setTitle("Search");


        categoryList = new ArrayList<>();
        birthYearList = new ArrayList<>();
        bloodGroupList = new ArrayList<>();
        heightList = new ArrayList<>();
        hghtList = new ArrayList<>();
        //qualificationList = new ArrayList<>();
        educationLevelList = new ArrayList<>();
        jobLocationList = new ArrayList<>();
        gotraList = new ArrayList<>();
        // hometownList = new ArrayList<>();
        professionList = new ArrayList<>();
        searchList = new ArrayList<>();
        yearList = new ArrayList<>();
        varnList = new ArrayList<>();

        session = new UserSessionManager(getContext());
        HashMap<String, Object> user = session.getUserDetails();
        userId = Integer.parseInt(String.valueOf(user.get(UserSessionManager.USERID)));

        showLoader = ShowLoader.getProgressDialog(getActivity());

        setSearchRecycler();

        getCategory(userId);
        getBirthYear();
        getBloodGroup();
        getHeight();
        getEducationLevel();
        //  getQualification();
        //getLocation();
        getGotra();
        getProfession();
        getVarna();

        if (!etFname.getText().toString().trim().equals("") && !etFname.getText().toString().trim().isEmpty())
            selFname = etFname.getText().toString().trim();

        if (!etMname.getText().toString().trim().equals("") && !etMname.getText().toString().trim().isEmpty())
            selMname = etMname.getText().toString().trim();

        if (!etLname.getText().toString().trim().equals("") && !etLname.getText().toString().trim().isEmpty())
            selLname = etLname.getText().toString().trim();

        if (!etEducation.getText().toString().trim().equals("") && !etEducation.getText().toString().trim().isEmpty())
            selEducation = etEducation.getText().toString().trim();

        if (!etHometown.getText().toString().trim().equals("") && !etHometown.getText().toString().trim().isEmpty())
            selHometown = etHometown.getText().toString().trim();

        /*if (!etVarn.getText().toString().trim().equals("") && !etVarn.getText().toString().trim().isEmpty())
            selVarn = etVarn.getText().toString().trim(); */

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selCategory = categoryList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBirthYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selBirthYear = birthYearList.get(position).trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selBloodGroup = bloodGroupList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selHeight = heightList.get(position).trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spEducationLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selEducationLevel = educationLevelList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  spEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selEducation = qualificationList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

       /* spJobLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selJobLocation = jobLocationList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        spGotra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selGotra = gotraList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* spHometown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selHometown = hometownList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        spVarn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                    selVarn = varnList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selProfession = professionList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetButtonClick();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonObject();

                if (jsonObject() != null && jsonObject().size() > 0) {
                    search(userId, jsonObject());
                } else {
                    Toast.makeText(getContext(), "Please enter some value", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBasicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonBasicObject();

                if (jsonBasicObject() != null && jsonBasicObject().size() > 0) {
                    search(userId, jsonBasicObject());
                } else {
                    Toast.makeText(getContext(), "Please enter some value", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private JsonObject jsonBasicObject() {

        basicObj = new JsonObject();

        if (etFname.getText().toString().trim() != null && !etFname.getText().toString().trim().equals("")) {
            basicObj.addProperty("firstname", etFname.getText().toString().trim());
        } else {
        }
        if (etMname.getText().toString().trim() != null && !etMname.getText().toString().trim().equals("")) {
            basicObj.addProperty("middlename", etMname.getText().toString().trim());
        } else {
        }
        if (etLname.getText().toString().trim() != null && !etLname.getText().toString().trim().equals("")) {
            basicObj.addProperty("lastname", etLname.getText().toString().trim());
        } else {
        }
        if (selCategory != null && !selCategory.equals("")) {
            basicObj.addProperty("category", selCategory);
        } else {
        }
        if (selEducationLevel != null && !selEducationLevel.equals("")) {
            basicObj.addProperty("educationLevel", selEducationLevel);
        } else {
        }

        return basicObj;

    }

    private void setSearchRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvSearch.setLayoutManager(linearLayoutManager);
        adapter = new SearchAdapter(getContext(), searchList, new SearchAdapter.GetUserList() {
            @Override
            public void getList() {
                if (jsonObject() != null && jsonObject().size() > 0) {
                    search(userId, jsonObject());
                } else {
                    Toast.makeText(getContext(), "Please enter some value", Toast.LENGTH_LONG).show();
                }
            }
        });
        rvSearch.setAdapter(adapter);
    }

    private void search(int userId, JsonObject obj) {
        searchList.clear();
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<SearchResultDTO> call = api.getSearchResult(userId, obj);

            call.enqueue(new Callback<SearchResultDTO>() {
                @Override
                public void onResponse(Call<SearchResultDTO> call, Response<SearchResultDTO> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().getResult().equals("Success")) {
                            if (response.body().getSearchResults().size() > 0) {
                                Toast.makeText(getContext(), "Search result displayed below successfully", Toast.LENGTH_SHORT).show();

                                searchList.clear();
                                searchList.addAll(response.body().getSearchResults());
                                adapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            searchList.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<SearchResultDTO> call, Throwable t) {

                }
            });

        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private JsonObject jsonObject() {

        obj = new JsonObject();

        if (etFname.getText().toString().trim() != null && !etFname.getText().toString().trim().equals("")) {
            obj.addProperty("firstname", etFname.getText().toString().trim());
        } else {
        }
        if (etMname.getText().toString().trim() != null && !etMname.getText().toString().trim().equals("")) {
            obj.addProperty("middlename", etMname.getText().toString().trim());
        } else {
        }
        if (etLname.getText().toString().trim() != null && !etLname.getText().toString().trim().equals("")) {
            obj.addProperty("lastname", etLname.getText().toString().trim());
        } else {
        }
        if (selCategory != null && !selCategory.equals("")) {
            obj.addProperty("category", selCategory);
        } else {
        }
        if (selEducationLevel != null && !selEducationLevel.equals("")) {
            obj.addProperty("educationLevel", selEducationLevel);
        } else {
        }
        if (selBirthYear != null && !selBirthYear.equals("")) {
            obj.addProperty("birthyear", selBirthYear);
        } else {
        }
        if (selHeight != null && !selHeight.equals("")) {
            obj.addProperty("height", selHeight);
        } else {
        }
        if (selBloodGroup != null && !selBloodGroup.equals("")) {
            obj.addProperty("bloodgroup", selBloodGroup);
        } else {
        }
        if (etEducationPlace.getText().toString().trim() != null && !etEducationPlace.getText().toString().trim().equals("")) {
            obj.addProperty("joblocation", etEducationPlace.getText().toString());
        } else {
        }
        if (etEducation.getText().toString().trim() != null && !etEducation.getText().toString().trim().equals("")) {
            obj.addProperty("education", etEducation.getText().toString().trim());
        } else {
        }

        if (selVarn != null && !selVarn.equals("")) {
            obj.addProperty("varn", selVarn);
        } else {
        }
        if (selGotra != null && !selGotra.equals("")) {
            obj.addProperty("gotra", selGotra);
        } else {
        }
        if (etHometown.getText().toString().trim() != null && !etHometown.getText().toString().trim().equals("")) {
            obj.addProperty("hometown", etHometown.getText().toString().trim());
        } else {
        }
        if (selProfession != null && !selProfession.equals("")) {
            obj.addProperty("profession", selProfession);
        } else {
        }
        return obj;
    }

    private void onResetButtonClick() {

        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.delete(i);
        }
        searchList.clear();
        adapter.notifyDataSetChanged();
        rvSearch.removeAllViews();
      //  rvSearch.setAdapter(null);
        rvSearch.removeAllViewsInLayout();

        selFname = "";
        selMname = "";
        selLname = "";
        selVarn = "";
        selCategory = "";
        selBirthYear = "";
        selBloodGroup = "";
        selHeight = "";
        selEducation = "";
        selJobLocation = "";
        selGotra = "";
        selHometown = "";
        selProfession = "";
        selEducationLevel = "";

        etFname.setText("");
        etMname.setText("");
        etLname.setText("");
        // etVarn.setText("");
        etEducation.setText("");
        etHometown.setText("");
        etEducationPlace.setText("");

        spCategory.setSelection(0);
        spEducationLevel.setSelection(0);
        spBirthYear.setSelection(0);
        spHeight.setSelection(0);
        spBloodGroup.setSelection(0);
        spProfession.setSelection(0);
        //spJobLocation.setSelection(0);
        spGotra.setSelection(0);
        spVarn.setSelection(0);

    }

    private void getCategory(int userId) {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getCategory(userId);
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        categoryList.addAll(response.body());
                        categoryList.add(0, getResources().getString(R.string.category));
                        ArrayAdapter aa0 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryList);
                        aa0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spCategory.setSelection(0);
                        spCategory.setAdapter(aa0);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getEducationLevel() {

        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getEducationLevel();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        educationLevelList.addAll(response.body());
                        educationLevelList.add(0, getResources().getString(R.string.education_level));
                        ArrayAdapter aa6 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, educationLevelList);
                        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spEducationLevel.setSelection(0);
                        spEducationLevel.setAdapter(aa6);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBirthYear() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getBirthYear();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        birthYearList.addAll(response.body());
                        birthYearList.add(0, getResources().getString(R.string.birth_year));
                        ArrayAdapter aa1 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, birthYearList);
                        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spBirthYear.setSelection(0);
                        spBirthYear.setAdapter(aa1);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }


            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHeight() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getHeight();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {

                        heightList.addAll(response.body());
                        heightList.add(0, getResources().getString(R.string.height));

                        ArrayAdapter aa3 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, heightList);
                        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spHeight.setSelection(0);
                        spHeight.setAdapter(aa3);
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBloodGroup() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getBloodGroup();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        bloodGroupList.addAll(response.body());
                        bloodGroupList.add(0, getResources().getString(R.string.bloodgroup));
                        ArrayAdapter aa2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, bloodGroupList);
                        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spBloodGroup.setSelection(0);
                        spBloodGroup.setAdapter(aa2);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getProfession() {

        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getProfession();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {

                        professionList.addAll(response.body());
                        professionList.add(0, getResources().getString(R.string.ocuupation));
                        ArrayAdapter aa7 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, professionList);
                        aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spProfession.setSelection(0);
                        spProfession.setAdapter(aa7);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

   /* private void getQualification() {

        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getQualification();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() >= 0) {
                        qualificationList.addAll(response.body());
                        qualificationList.add(0, getResources().getString(R.string.education));
                        ArrayAdapter aa6 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, qualificationList);
                        aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spEducation.setSelection(0);
                        spEducation.setAdapter(aa6);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*public void getLocation() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ResponseLocation> call = api.getLocation();
            call.enqueue(new Callback<ResponseLocation>() {
                @Override
                public void onResponse(Call<ResponseLocation> call, Response<ResponseLocation> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body() != null) {
                        if (response.body().getDistrict().size() > 0) {
                            jobLocationList.addAll(response.body().getDistrict());
                            jobLocationList.add(0, getResources().getString(R.string.job_location));
                            ArrayAdapter aa6 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, jobLocationList);
                            aa6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                           // spJobLocation.setSelection(0);
                            //spJobLocation.setAdapter(aa6);
                        }else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                        *//*if (response.body().getHomeTown().size() >= 0) {
                            hometownList.addAll(response.body().getHomeTown());
                            hometownList.add(0, getResources().getString(R.string.hometown));
                            ArrayAdapter aa8 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, hometownList);
                            aa8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spHometown.setSelection(0);
                            spHometown.setAdapter(aa8);

                        }*//*
                    }
                }

                @Override
                public void onFailure(Call<ResponseLocation> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void getGotra() {

        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getGotra();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        gotraList.addAll(response.body());
                        gotraList.add(0, getResources().getString(R.string.gotra));
                        ArrayAdapter aa7 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, gotraList);
                        aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spGotra.setSelection(0);
                        spGotra.setAdapter(aa7);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void getVarna() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<String>> call = api.getVarna();
            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().size() > 0) {
                        varnList.addAll(response.body());
                        varnList.add(0, getResources().getString(R.string.varn));
                        ArrayAdapter aa7 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, varnList);
                        aa7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spVarn.setSelection(0);
                        spVarn.setAdapter(aa7);
                    }else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                    showLoader.hide();
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
