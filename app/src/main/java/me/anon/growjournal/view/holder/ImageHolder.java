package me.anon.growjournal.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import lombok.Data;
import me.anon.growjournal.MainApplication;
import me.anon.growjournal.R;
import me.anon.growjournal.manager.PlantManager;

/**
 * // TODO: Add class description
 */
@Data
public class ImageHolder extends RecyclerView.ViewHolder
{
	private ImageView image;

	public ImageHolder(View itemView)
	{
		super(itemView);

		image = (ImageView)itemView.findViewById(R.id.image);
	}

	public void populate(String imagePath)
	{
		ImageLoader.getInstance().cancelDisplayTask(image);
		ImageAware imageAware = new ImageViewAware(image, true);

		String[] folders = imagePath.split("/");
		String thumbPath = folders[folders.length - 2] + "/thumb/" + folders[folders.length - 1];
		ImageLoader.getInstance().displayImage("file://" + PlantManager.imagesPath + thumbPath, imageAware, MainApplication.getDisplayImageOptions());

		itemView.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View v)
			{
//					Intent details = new Intent(v.getContext(), ImageLightboxDialog.class);
//					details.putExtra("images", (String[])images.toArray(new String[getItemCount()]));
//					details.putExtra("image_position", position);
//					v.getContext().startActivity(details);
			}
		});
	}
}
