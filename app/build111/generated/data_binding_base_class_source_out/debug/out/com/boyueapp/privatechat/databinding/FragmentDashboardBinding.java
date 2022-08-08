// Generated by view binder compiler. Do not edit!
package com.boyueapp.privatechat.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.boyueapp.privatechat.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentDashboardBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ListView MultiChatUsers;

  @NonNull
  public final ScrollView scrollDash;

  @NonNull
  public final ScrollView scrollDashboard;

  private FragmentDashboardBinding(@NonNull ConstraintLayout rootView,
      @NonNull ListView MultiChatUsers, @NonNull ScrollView scrollDash,
      @NonNull ScrollView scrollDashboard) {
    this.rootView = rootView;
    this.MultiChatUsers = MultiChatUsers;
    this.scrollDash = scrollDash;
    this.scrollDashboard = scrollDashboard;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_dashboard, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentDashboardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.MultiChatUsers;
      ListView MultiChatUsers = ViewBindings.findChildViewById(rootView, id);
      if (MultiChatUsers == null) {
        break missingId;
      }

      id = R.id.scroll_dash;
      ScrollView scrollDash = ViewBindings.findChildViewById(rootView, id);
      if (scrollDash == null) {
        break missingId;
      }

      id = R.id.scroll_dashboard;
      ScrollView scrollDashboard = ViewBindings.findChildViewById(rootView, id);
      if (scrollDashboard == null) {
        break missingId;
      }

      return new FragmentDashboardBinding((ConstraintLayout) rootView, MultiChatUsers, scrollDash,
          scrollDashboard);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}