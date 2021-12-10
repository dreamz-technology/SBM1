package com.dreamz.samtaBhratruMandal.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dreamz.samtaBhratruMandal.R;


public class AboutUsFragment extends Fragment {



    public AboutUsFragment() {
        // Required empty public constructor
    }

    View view;
    TextView tvContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        tvContent= view.findViewById(R.id.tv_content);
        tvContent.setText(Html.fromHtml("<br><b>समता भ्रातृ मंडळ,पिंपरी चिंचवड, पुणे <br> <br> बंध आयुष्यचे, नाती आयुष्याची <br> <br> </b> लेवा समाजाचे एक मंडळ असावे या भावनेने मार्च १९९७ मध्ये पिंपरी चिंचवड परिसरत वास्तव्याला असलेले काही लेवा समाज बांधवांनी एक समता भ्रातृमंडळाची सुरुवात झाली. पहिल्याच वर्षी देहू येथील भंडारा डोंगरावर परिसरातील समाज बांधवांना एकत्र आणून स्नेहामेळावा आयोजित केला. या मेळाव्याला समाजबांधवांचा मिळालेला प्रतिसाद लक्षात घेता मंडळ स्थापनेविषयी चर्चा झाली व पदाधिकाऱ्यांची निवड करण्यात आली. मंडळाचे कार्यक्षेत्र खालीलप्रमाणे निश्चित करण्यात आले.<br> <br> १. शैक्षणिक:लहान मुलांना शिक्षणाची आवड व शिस्त निर्माण करणेसाठी शिशुकेंद्र सुरू करणे. समाजातील गरीब व गरजू विद्यार्थ्यांना शालेय साहित्य उपलब्ध करणे. <br> २. वैद्यकीय: समाजातील गरीब व गरजू रुग्णांना आरोग्यसेवा उपलब्ध करून देण्यासाठी विविध आरोग्य शिबिरांचे आयोजन करणे. रुग्णांना योग्य ती वैद्यकीय व आर्थिक मदत करणे. <br> ३. क्रीडा: समाजातील मुलांच्या क्रीडा कौशल्याला वाव देण्यासाठी विविध क्रीडा स्पर्धांचे आयोजन करणे. <br> ४. उद्योगविषयी: नामांकित उद्योगपतींच्या अनुभवी मार्गदर्शनाचा लाभ नवोदित उद्योजकांना करून देणे. <br> <br> <br> <b> समता भ्रातृ मंडळाचे उपक्रम <br> <br> </b> समता भ्रातृमंडळाची प्रगती आज विविध प्रकारच्या सामाजिक, शैक्षणिक, औद्योगिक, कौटुंबिक व वैद्यकीय क्षेत्रांच्या माध्यमातून दिसत आहे. आशा प्रकारे एक एक उपक्रम वाढवत मंडळाचे आता खालील उपक्रम आहेत. <br> <br> १.स्वातंत्र्य दिन, प्रजासत्ताक दिन व महाराष्ट्र दिन साजरा करणे. <br> २.कामगार दिनाचे औचित्य साधून सेवा निवृत्त कामगारांचा सत्कार करणे. <br> ३.सेवानिवृत्त, वारकरी, ज्येष्ठ नागरिक, नव विवाहित दाम्पत्य, यशस्वी उद्योजकांचा सत्कार करणे. <br> ४.मंडळाच्या सभासदांचा कौटुंबिक स्नेहामेळावा. <br> ५.वर्षापुष्प प्रकाशन . <br> ६.वधु - वर मेळावा. <br> ७.शैक्षणिक गुणवंतांचा सत्कार. <br> ८.अँड्रॉइड ऍप्लिकेशन. <br> <br> मंडळाचे शुभचिंतक, दानशूर समाजबांधव, कार्यकारणी सदस्य, कार्यकर्ते, समस्त लेवा समाजबांधव यांच्या अथक प्रयत्नातून आपले लेवा संघटन मजबूत होत आहे. मंडळाच्या प्रगतीसाठी आर्थिक बाजू देखील भक्कम हवीच, त्यासाठी समाज बांधवांनी देणगी दिल्यास मंडळ त्यांचे ऋणी राहील. <br><br><br>"));

        return view;
    }
}