package com.example.lht.frescotest;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://n.sinaimg.cn/sinacn/w1836h1836/20180311/a028-fxpwyhx0629231.jpg";
    String url2 ="http://n.sinaimg.cn/sinacn/w1836h1836/20180311/1bde-fxpwyhx0629742.jpg";
    //http://img3.imgtn.bdimg.com/it/u=4181309268,1833832416&fm=214&gp=0.jpg
    //低分辨率图片模拟模糊图片
    String url2_1 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3533155993,2484124234&fm=11&gp=0.jpg";
    //模拟高分辨率图
    String url2_2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549016629181&di=c737967a4661bb38f4ce588467c5603c&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01655358551cd3a8012060c84bb1b4.gif";
    String url3 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=360742459,544648173&fm=26&gp=0.jpg";
    //gif
    String url4 ="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1548861661594&di=ca18548737f7663bd554575a5ac5c038&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2Fattachments2%2F201410%2F16%2F1144349mwgw1xwmgz6w9m1.gif";
    SimpleDraweeView simpleDraweeView;
    ControllerListener<ImageInfo> listener;


    List<Drawable> backgroundsList;
    List<Drawable> overlaysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        simpleDraweeView = findViewById(R.id.simpleDraweeView);

        //initView1();//渐进式显示网络图片
//       initView2();//多图请求--先显示模糊图片，等到大图加载成功后再显示大图
        // initView3();//多种效果结合：加载图片成功和失败
       /*initView4();//事件监听*/
        initView5();//控制动画图播放


    }

    /**
     *  自动控制动画图播放
     */
    private void initView5() {
        //加载本地 gif图
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.mipmap.kimmy))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//                .setUri(url4)
                .setUri(uri)//加载本地gif
                .setControllerListener(listener)
                //设置动画图自动播放
                .setAutoPlayAnimations(true)
                .build();

        simpleDraweeView.setController(draweeController);
    }

    /**
     *  事件监听
     */
    private void initView4() {
        ControllerListener<ImageInfo> listener = new ControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {

            }

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {

                //控制动画图播放
                if (anim != null){
                    anim.start();
                }

            }

            //图片加载成功的时候
            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {

            }

            //图片加载失败的时候
            @Override
            public void onFailure(String id, Throwable throwable) {

            }

            @Override
            public void onRelease(String id) {

            }
        };
    }

    /**
     * 多种效果结合：加载图片成功和失败
     * 如果成功那么显示加载后的图片，
     * 如果失败，那么显示重新加载的图片，
     * 如果重复了四次仍然无法完成加载，
     * 那么就显示加载失败的图片
     */
    private void initView3() {
        DraweeController successController = Fresco.newDraweeControllerBuilder()
                //想看到失败的效果，设置错误的uri
                //.setUri("http://avatar.csdn.net/4/E/8/1_y1scp.jpg")
                .setUri("http://afds41616.jpg")
                .setTapToRetryEnabled(true)
                .setOldController(simpleDraweeView.getController())
                .build();
         simpleDraweeView.setController(successController);
    }

    /**
     * 多图请求--先显示模糊图片，等到大图加载成功后再显示大图
     */
    private void initView2() {
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                //低分辨率
                .setLowResImageRequest(ImageRequest.fromUri(url2_1))
                //高分辨率
                .setImageRequest(ImageRequest.fromUri(url4))
                .build();
        simpleDraweeView.setController(draweeController);
    }

    /**
     * 渐进式显示网络图片
     */
    private void initView1() {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url2))
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);
    }
}
