package comp5216.sydney.edu.au.timebank;

import android.app.Activity;
import android.os.Bundle;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import comp5216.sydney.edu.au.timebank.adapter.SliderAdaptor;

public class TaskDetailsActivity extends Activity {
    private int[] images={R.drawable.one,R.drawable.two,R.drawable.three};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);
        SliderView sliderView=findViewById(R.id.image_slider);
        SliderAdaptor sliderAdaptor=new SliderAdaptor(images);
        sliderView.setSliderAdapter(sliderAdaptor);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }
}
