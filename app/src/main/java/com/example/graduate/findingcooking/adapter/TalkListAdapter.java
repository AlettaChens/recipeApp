package com.example.graduate.findingcooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.graduate.findingcooking.R;
import com.example.graduate.findingcooking.bean.Talk;
import com.example.graduate.findingcooking.utils.GlideX;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TalkListAdapter extends RecyclerView.Adapter<TalkListAdapter.TalkHolder> {
	private List<Talk> talkList;
	private Context context;
	private TalkClickListener talkClickListener;
	private String updateType;

	public TalkListAdapter(Context context, List<Talk> talkList, @NonNull TalkClickListener talkClickListener) {
		this.talkList = talkList;
		this.context = context;
		this.talkClickListener = talkClickListener;
	}

	@NonNull
	@Override
	public TalkHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_talk_list, viewGroup, false);
		return new TalkHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull TalkHolder talkHolder, int i) {
		talkHolder.update(talkList.get(i), i);
	}

	@Override
	public int getItemCount() {
		return talkList.size();
	}

	public class TalkHolder extends RecyclerView.ViewHolder {

		private CircleImageView user_avatar;
		private TextView userName;
		private TextView publish_time;
		private CheckBox star_logo;
		private int position;

		public TalkHolder(@NonNull View itemView) {
			super(itemView);
			user_avatar = itemView.findViewById(R.id.user_avatar);
			userName = itemView.findViewById(R.id.userName);
			publish_time = itemView.findViewById(R.id.publish_time);
			star_logo = itemView.findViewById(R.id.star_logo);
			star_logo.setOnClickListener(new View.OnClickListener() {//防止checkBox事件冲突
				@Override
				public void onClick(View v) {
					int starCurrent = Integer.parseInt(star_logo.getTag().toString());
					if (star_logo.isChecked()) {
						updateType = "增加";
						starCurrent++;
						star_logo.setChecked(true);
					} else {
						updateType = "减少";
						starCurrent--;
						star_logo.setChecked(false);
					}
					star_logo.setTag(String.valueOf(starCurrent));
					star_logo.setText(String.valueOf(starCurrent));
					talkClickListener.onStarChange(talkList.get(position).getFormu().getId(), updateType);
				}
			});

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					talkClickListener.TalkListItemClick(talkList.get(position));
				}
			});
		}

		public void update(Talk item, int i) {
			position = i;
			GlideX.getInstance().loadImage(context, item.getFormu().getUserAvatar(), user_avatar);
			userName.setText(item.getFormu().getUserName());
			publish_time.setText(item.getFormu().getDate());
			star_logo.setTag(String.valueOf(item.getFormu().getStar()));
			star_logo.setText(String.valueOf(item.getFormu().getStar()));
			if (item.getFormu().getIsCheck()== 1) {
				star_logo.setChecked(true);
			} else {
				star_logo.setChecked(false);
			}
		}
	}

	public interface TalkClickListener {
		void TalkListItemClick(Talk talk);

		void onStarChange(long forumId, String changeType);
	}
}
