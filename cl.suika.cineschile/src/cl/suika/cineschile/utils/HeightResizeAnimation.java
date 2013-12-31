package cl.suika.cineschile.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightResizeAnimation extends Animation {
	int targetWidth;
	int originaltWidth;
	View view;
	boolean expand;
	int newWidth = 0;
	boolean fillParent;

	public HeightResizeAnimation(View view, int targetWidth, boolean fillParent) {
		this.view = view;
		this.originaltWidth = this.view.getMeasuredHeight();
		this.targetWidth = targetWidth;
		newWidth = originaltWidth;
		if (originaltWidth > targetWidth) {
			expand = false;
		} else {
			expand = true;
		}
		this.fillParent = fillParent;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (expand && newWidth < targetWidth) {
			newWidth = (int) (newWidth + (targetWidth - newWidth) * interpolatedTime);
		}

		if (!expand && newWidth > targetWidth) {
			newWidth = (int) (newWidth - (newWidth - targetWidth) * interpolatedTime);
		}
		if (fillParent && interpolatedTime == 1.0) {
			view.getLayoutParams().height = -1;

		} else {
			view.getLayoutParams().height = newWidth;
		}
		view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}

}