package com.eucleia.pdicheck.activity.normal;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.activity.BaseActivity;
import com.eucleia.pdicheck.databinding.ActLoginBinding;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.mvpview.BaseNetMvpView;
import com.eucleia.pdicheck.net.presenter.LoginPresenter;
import com.eucleia.pdicheck.widget.ETextWatcher;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private boolean isEyeOpen = false;
    private ActLoginBinding binding;
    private final SingleClickListener eyeClick = view -> {
        isEyeOpen = !isEyeOpen;
        eyeOnChange();
    };


    private final SingleClickListener loginListener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            CharSequence userName = binding.username.getText();
            CharSequence passWord = binding.password.getText();
            if (TextUtils.isEmpty(userName)) {
                binding.setError(ResUtils.getString(R.string.user_unwrite));
                binding.setType(1);
                return;
            }
            if (TextUtils.isEmpty(passWord)) {
                binding.setError(ResUtils.getString(R.string.pwd_unwrite));
                binding.setType(2);
                return;
            }

           LoginPresenter.get().login(userName.toString(), passWord.toString());

        }
    };
    private final BaseNetMvpView loginNetMvpView = new BaseNetMvpView() {
        @Override
        public void Empty() {

        }

        @Override
        public void NetSuccess() {
            startActivity(MainActivity.class);
        }

        @Override
        public void NetFail(String error) {
            binding.setError(error);
            binding.setType(2);
        }
    };
    private final ETextWatcher textWatcher = new ETextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            binding.setType(0);
        }
    };

    @Override
    protected ViewDataBinding bindAct() {
        binding = ActLoginBinding.inflate(getLayoutInflater());
        return binding;
    }


    @Override
    protected void initView() {
        binding.setListener(loginListener);
        binding.setEyeClick(eyeClick);
        binding.useApp.setText(String.format(ResUtils.getString(R.string.use_app),
                ResUtils.getString(R.string.app_name)));
        binding.username.setText("zqz");
        binding.password.setText("Sgmw5050");
        binding.username.addTextChangedListener(textWatcher);
        binding.password.addTextChangedListener(textWatcher);
        LoginPresenter.get().attachView(loginNetMvpView);
        eyeOnChange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginPresenter.get().detachView(loginNetMvpView);
    }

    /**
     * 切换显示密码
     */
    private void eyeOnChange() {
        if (isEyeOpen) {
            binding.eye.setImageResource(R.drawable.ic_eye_open);
            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.eye.setImageResource(R.drawable.ic_eye_close);
            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
