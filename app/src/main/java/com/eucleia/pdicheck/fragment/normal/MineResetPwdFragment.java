package com.eucleia.pdicheck.fragment.normal;

import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.databinding.FragMineResetPwdBinding;
import com.eucleia.pdicheck.databinding.LayoutHeadNormalBinding;
import com.eucleia.pdicheck.fragment.BaseFragment;
import com.eucleia.pdicheck.listener.SingleClickListener;
import com.eucleia.pdicheck.net.presenter.ResetPwdPresenter;
import com.eucleia.tabscanap.util.EToastUtils;
import com.eucleia.tabscanap.util.ResUtils;

/**
 * 修改密码
 */
public class MineResetPwdFragment extends BaseFragment {

    private FragMineResetPwdBinding binding;
    private LayoutHeadNormalBinding headNormal;
    private SingleClickListener singleClickListener = new SingleClickListener() {
        @Override
        public void singleClick(View view) {
            String oldPwd = binding.oldPwd.getText().toString();
            String newPwd = binding.newPwd.getText().toString();
            if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)) {
                EToastUtils.get().showShort(R.string.pwd_unwrite, false);
                return;
            }
            ResetPwdPresenter.get().resetPwd(oldPwd, newPwd);
        }
    };


    @Override
    protected ViewDataBinding bindFrag(LayoutInflater inflater, ViewGroup container) {
        if (binding == null) {
            binding = FragMineResetPwdBinding.inflate(inflater, container, false);
            headNormal = binding.headNormal;
        }
        return binding;
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_mine_reset_pwd;
    }

    @Override
    protected void initLayout() {
        headNormal.setHead(ResUtils.getString(R.string.change_pwd));
        binding.setListener(singleClickListener);


        binding.oldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
