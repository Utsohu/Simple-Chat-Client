// Generated by view binder compiler. Do not edit!
package com.boyueapp.privatechat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.boyueapp.privatechat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button acceptBtn;

  @NonNull
  public final ConstraintLayout coordinatorLayout;

  @NonNull
  public final EditText editUserName;

  @NonNull
  public final EditText editUserPassword;

  @NonNull
  public final TextView loginErrorMsgView;

  @NonNull
  public final Button registerBtn;

  private ActivityLoginBinding(@NonNull ConstraintLayout rootView, @NonNull Button acceptBtn,
      @NonNull ConstraintLayout coordinatorLayout, @NonNull EditText editUserName,
      @NonNull EditText editUserPassword, @NonNull TextView loginErrorMsgView,
      @NonNull Button registerBtn) {
    this.rootView = rootView;
    this.acceptBtn = acceptBtn;
    this.coordinatorLayout = coordinatorLayout;
    this.editUserName = editUserName;
    this.editUserPassword = editUserPassword;
    this.loginErrorMsgView = loginErrorMsgView;
    this.registerBtn = registerBtn;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.acceptBtn;
      Button acceptBtn = ViewBindings.findChildViewById(rootView, id);
      if (acceptBtn == null) {
        break missingId;
      }

      ConstraintLayout coordinatorLayout = (ConstraintLayout) rootView;

      id = R.id.editUserName;
      EditText editUserName = ViewBindings.findChildViewById(rootView, id);
      if (editUserName == null) {
        break missingId;
      }

      id = R.id.editUserPassword;
      EditText editUserPassword = ViewBindings.findChildViewById(rootView, id);
      if (editUserPassword == null) {
        break missingId;
      }

      id = R.id.loginErrorMsgView;
      TextView loginErrorMsgView = ViewBindings.findChildViewById(rootView, id);
      if (loginErrorMsgView == null) {
        break missingId;
      }

      id = R.id.registerBtn;
      Button registerBtn = ViewBindings.findChildViewById(rootView, id);
      if (registerBtn == null) {
        break missingId;
      }

      return new ActivityLoginBinding((ConstraintLayout) rootView, acceptBtn, coordinatorLayout,
          editUserName, editUserPassword, loginErrorMsgView, registerBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
