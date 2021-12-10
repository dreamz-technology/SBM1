//
//package com.dreamz.SamataBhratruMandal.Fragments;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.dreamz.SamataBhratruMandal.Adapters.BasicDetailsAdapter;
//import com.dreamz.SamataBhratruMandal.Models.BasicDetailsModel;
//import com.dreamz.SamataBhratruMandal.R;
//import com.dreamz.SamataBhratruMandal.Utils.ShowLoader;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.TextHttpResponseHandler;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.dreamz.SamataBhratruMandal.Server.serverConstants.GetAllCandidateSearchResult;
//
//
//public class HomeFragment extends Fragment {
//
//    private RecyclerView recyclerView;
//    private  BasicDetailsAdapter basicDetailsAdapter;
//    private List<BasicDetailsModel>userList;
//    private ShowLoader showLoader;
//
//    ProgressDialog prgDialog;
//
//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        showLoader = new ShowLoader(getContext());
//        userList = new ArrayList<>();
//        basicDetailsAdapter = new BasicDetailsAdapter(getContext());
//        recyclerView = view.findViewById(R.id.recycler_get_all_users);
//
//
//        API_Call();
//        return view;
//    }
//
//    private void API_Call() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params;
//        showLoader.showDialog();
//        params = new RequestParams();
//        client.get(GetAllCandidateSearchResult,
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
//                        showLoader.dismissDialog();
//                        JSONArray array = null;
//
//                        try {
//                            array = new JSONArray(responseString);
//                            Log.d("TAG"," Response is:-"+responseString);
//
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject product = array.getJSONObject(i);
//                                userList.add(new BasicDetailsModel(
//                                        product.getString("profileImage"),
//                                        product.getString("fullName"),
//                                        product.getString("education"),
//                                        product.getString("birthdate")));
//
//
//                                recyclerView.setAdapter(basicDetailsAdapter);
//                                LinearLayoutManager layoutManager = new  LinearLayoutManager(getContext());
//
//                                recyclerView .setLayoutManager(layoutManager);
//                                basicDetailsAdapter.addItem(userList);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            // Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
//                        Log.d("TAG","Error Response:-"+responseString);
//                        if (statusCode == 404) {
//                            Toast.makeText(getContext(),
//                                    "Requested resource not found",
//                                    Toast.LENGTH_LONG).show();
//                        } else if (statusCode == 500) {
//                            Toast.makeText(getContext(),
//                                    "Something went wrong at server end",
//                                    Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(
//                                    getContext(),
//                                    "Please connect to Internet and try again!",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//
//    }
////
////    @Override
////    public void rv_click(View v, int position) {
////        Toast.makeText(getContext(), "clicked on " +position, Toast.LENGTH_SHORT).show();
////        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
////        startActivity(intent);
////    }
//}
//

package com.dreamz.samtaBhratruMandal.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dreamz.samtaBhratruMandal.Activities.ApprovedListActivity;
import com.dreamz.samtaBhratruMandal.Activities.BookMarks;
import com.dreamz.samtaBhratruMandal.Activities.MainActivity;
import com.dreamz.samtaBhratruMandal.Activities.ProfileActivity;
import com.dreamz.samtaBhratruMandal.Adapters.SliderAdapterExample;
import com.dreamz.samtaBhratruMandal.Interface.APIClient;
import com.dreamz.samtaBhratruMandal.Interface.ApiFactory;
import com.dreamz.samtaBhratruMandal.Models.BannerDTO;
import com.dreamz.samtaBhratruMandal.R;
import com.dreamz.samtaBhratruMandal.Server.serverConstants;
import com.dreamz.samtaBhratruMandal.Utils.ShowLoader;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    CardView cardProfile, cardSearchProfile, cardBookMArks, cardTimeSchedule, cardAboutUs, cardApprovedLIst;
    SliderView sliderView;
    SliderAdapterExample adapter;
    TextView DreamzLink;
    ShowLoader showLoader;
    ArrayList<BannerDTO> imageList = new ArrayList<BannerDTO>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        adapter = new SliderAdapterExample(getContext(), imageList);
        sliderView = view.findViewById(R.id.imageSlider);

        cardProfile = view.findViewById(R.id.card_profile);
        cardTimeSchedule = view.findViewById(R.id.card_meeting_schedule);
        cardAboutUs = view.findViewById(R.id.card_about_us);
        cardApprovedLIst = view.findViewById(R.id.card_approved_list);
        cardSearchProfile = view.findViewById(R.id.card_search_profile);
        cardBookMArks = view.findViewById(R.id.card_book_marks);

        DreamzLink = view.findViewById(R.id.dreamz_txt);
        showLoader = ShowLoader.getProgressDialog(getActivity());
        addSlider();
        //callDreamzUrl();Da
        clickEvents();

        return view;
    }

    private void clickEvents() {
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });


        cardBookMArks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BookMarks.class));
            }
        });

        cardTimeSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleMettingFragment scheduleMettingFragment = new ScheduleMettingFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, scheduleMettingFragment);
                fragmentTransaction.commit();
                //Toast.makeText(getContext(),"Hello cardTimeSchedule",Toast.LENGTH_SHORT).show();
            }
        });

        cardAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterestFragment aboutUsFragment = new InterestFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, aboutUsFragment);
                fragmentTransaction.commit();


                //   Toast.makeText(getContext(),"Work In Progress",Toast.LENGTH_SHORT).show();
            }
        });

        cardSearchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  SearchFragment fragment2 = new SearchFragment();
                SearchFilterFragment fragment2 = new SearchFilterFragment();
                // FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        cardApprovedLIst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"Work In Progress",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), ApprovedListActivity.class));
            }
        });

    }

    private void callDreamzUrl() {
        DreamzLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://dreamztechnology.com/"));
                startActivity(browserIntent);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (showLoader != null) {
            showLoader.dismiss();
        }
    }

    private void addSlider() {
        if (serverConstants.isConnectingToInternet(getActivity())) {
            showLoader.show();
            APIClient api = ApiFactory.create();
            Call<ArrayList<BannerDTO>> call = api.getBanners();
            call.enqueue(new Callback<ArrayList<BannerDTO>>() {
                @Override
                public void onResponse(Call<ArrayList<BannerDTO>> call, Response<ArrayList<BannerDTO>> response) {
                    showLoader.hide();
                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().size() > 0) {

                            imageList.addAll(response.body());
                            //  adapter.addItem(imageList);
                            sliderView.setSliderAdapter(adapter);
                            sliderView.setScrollTimeInSec(5);
                            adapter.notifyDataSetChanged();
                            sliderView.setAutoCycle(true);
                            sliderView.setIndicatorEnabled(false);
                            //   sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            //  sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            //  sliderView.setIndicatorSelectedColor(Color.WHITE);
                            //    sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.startAutoCycle();


                        }else{
                            Toast.makeText(getContext(), "No Banner Available", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onFailure(Call<ArrayList<BannerDTO>> call, Throwable t) {
                    showLoader.hide();
                    t.printStackTrace();
                }
            });

        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}