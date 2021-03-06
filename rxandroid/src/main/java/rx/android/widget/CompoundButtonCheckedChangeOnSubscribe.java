package rx.android.widget;

import android.widget.CompoundButton;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class CompoundButtonCheckedChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  private final CompoundButton view;

  public CompoundButtonCheckedChangeOnSubscribe(CompoundButton view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    checkUiThread();

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        subscriber.onNext(isChecked);
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnCheckedChangeListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnCheckedChangeListener(listener);

    // Send out the initial value.
    subscriber.onNext(view.isChecked());
  }
}
