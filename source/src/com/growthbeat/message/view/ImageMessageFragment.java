package com.growthbeat.message.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;

import com.growthbeat.message.GrowthMessage;
import com.growthbeat.message.model.Button;
import com.growthbeat.message.model.CloseButton;
import com.growthbeat.message.model.ImageButton;
import com.growthbeat.message.model.ImageMessage;

public class ImageMessageFragment extends Fragment {

	private ImageMessage imageMessage = null;
	private int loaderId = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		Object message = getArguments().get("message");
		if (message == null)
			return null;
		if (!(message instanceof ImageMessage))
			return null;

		this.imageMessage = (ImageMessage) message;

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		double availableWidth = Math.min(imageMessage.getPicture().getWidth() * displayMetrics.density, displayMetrics.widthPixels * 0.75);
		double availableHeight = Math.min(imageMessage.getPicture().getHeight() * displayMetrics.density,
				displayMetrics.heightPixels * 0.75);
		double ratio = Math.min(availableWidth / imageMessage.getPicture().getWidth(), availableHeight
				/ imageMessage.getPicture().getHeight());

		int width = (int) (imageMessage.getPicture().getWidth() * ratio);
		int height = (int) (imageMessage.getPicture().getHeight() * ratio);
		int left = (int) ((displayMetrics.widthPixels - width) / 2);
		int top = (int) ((displayMetrics.heightPixels - height) / 2);

		Rect rect = new Rect(left, top, width, height);

		FrameLayout baseLayout = new FrameLayout(getActivity());
		baseLayout.setBackgroundColor(Color.argb(128, 0, 0, 0));

		showImage(baseLayout, rect, ratio);
		showImageButtons(baseLayout, rect, ratio);
		showCloseButton(baseLayout, rect, ratio);

		return baseLayout;

	}

	private void showImage(FrameLayout baseLayout, Rect rect, double ratio) {

		UrlImageView urlImageView = new UrlImageView(getActivity(), imageMessage.getPicture().getUrl());
		urlImageView.setScaleType(ScaleType.FIT_CENTER);

		baseLayout.addView(wrapViewWithAbsoluteLayout(urlImageView, rect));

		getActivity().getSupportLoaderManager().initLoader(loaderId++, null, urlImageView);

	}

	private void showImageButtons(FrameLayout baseLayout, Rect rect, double ratio) {

		List<Button> buttons = extractButtons(Button.Type.image);

		int top = rect.getTop() + rect.getHeight();
		for (Button button : buttons) {

			final ImageButton imageButton = (ImageButton) button;

			int width = (int) (imageButton.getPicture().getWidth() * ratio);
			int height = (int) (imageButton.getPicture().getHeight() * ratio);
			int left = rect.getLeft() + (rect.getWidth() - width) / 2;
			top -= height;

			UrlImageButton urlImageButton = new UrlImageButton(getActivity(), imageButton.getPicture().getUrl());
			urlImageButton.setScaleType(ScaleType.FIT_CENTER);
			urlImageButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
					GrowthMessage.getInstance().selectButton(imageButton, imageMessage);
				}
			});

			getActivity().getSupportLoaderManager().initLoader(loaderId++, null, urlImageButton);
			baseLayout.addView(wrapViewWithAbsoluteLayout(urlImageButton, new Rect(left, top, width, height)));

		}

	}

	private void showCloseButton(FrameLayout baseLayout, Rect rect, double ratio) {

		List<Button> buttons = extractButtons(Button.Type.close);

		if (buttons.size() < 1)
			return;

		final CloseButton closeButton = (CloseButton) buttons.get(0);

		int width = (int) (closeButton.getPicture().getWidth() * ratio);
		int height = (int) (closeButton.getPicture().getHeight() * ratio);
		int left = rect.getLeft() + rect.getWidth() - width / 2;
		int top = rect.getTop() - height / 2;

		UrlImageButton urlImageButton = new UrlImageButton(getActivity(), closeButton.getPicture().getUrl());
		urlImageButton.setScaleType(ScaleType.FIT_CENTER);
		urlImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				GrowthMessage.getInstance().selectButton(closeButton, imageMessage);
			}
		});
		getActivity().getSupportLoaderManager().initLoader(loaderId++, null, urlImageButton);

		baseLayout.addView(wrapViewWithAbsoluteLayout(urlImageButton, new Rect(left, top, width, height)));

	}

	private List<Button> extractButtons(Button.Type type) {

		List<Button> buttons = new ArrayList<Button>();

		for (Button button : imageMessage.getButtons()) {
			if (button.getType() == type) {
				buttons.add(button);
			}
		}

		return buttons;

	}

	private View wrapViewWithAbsoluteLayout(View view, Rect rect) {

		FrameLayout frameLayout = new FrameLayout(getActivity());
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(rect.getWidth(), rect.getHeight());
		layoutParams.setMargins(rect.getLeft(), rect.getTop(), 0, 0);
		frameLayout.setLayoutParams(layoutParams);

		view.setLayoutParams(new ViewGroup.LayoutParams(rect.getWidth(), rect.getHeight()));
		frameLayout.addView(view);

		return frameLayout;

	}

	private class Rect {

		private int left;
		private int top;
		private int width;
		private int height;

		public Rect() {
			super();
		}

		public Rect(int left, int top, int width, int height) {
			this();
			setLeft(left);
			setTop(top);
			setWidth(width);
			setHeight(height);
		}

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getTop() {
			return top;
		}

		public void setTop(int top) {
			this.top = top;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

	}

}
