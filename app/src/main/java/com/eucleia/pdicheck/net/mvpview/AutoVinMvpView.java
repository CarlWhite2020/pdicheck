package com.eucleia.pdicheck.net.mvpview;

public interface AutoVinMvpView extends BaseMvpView{

   void onProgress(int progress,int max);

   void onComplete();

}
