package me.anon.growjournal.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Getter;
import me.anon.growjournal.R;
import me.anon.growjournal.model.tracker.Action;
import me.anon.growjournal.model.tracker.Additive;
import me.anon.growjournal.model.tracker.Plant;
import me.anon.growjournal.view.holder.ActionHolder;

/**
 * // TODO: Add class description
 */
public class ActionAdapter extends RecyclerView.Adapter<ActionHolder>
{
	@Getter private Plant plant;
	@Getter private List<Action> actions = new ArrayList<>();

	public void setActions(Plant plant, List<Action> actions)
	{
		this.plant = plant;
		this.actions = actions;
	}

	@Override public ActionHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		return new ActionHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.action_item, viewGroup, false));
	}

	@Override public void onBindViewHolder(final ActionHolder viewHolder, final int i)
	{
		final Action action = actions.get(i);

		if (action == null) return;

		DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(viewHolder.itemView.getContext());
		DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(viewHolder.itemView.getContext());

		Date actionDate = new Date(action.getDate());
		Calendar actionCalendar = GregorianCalendar.getInstance();
		actionCalendar.setTime(actionDate);
		String fullDateStr = dateFormat.format(actionDate) + " " + timeFormat.format(actionDate);

		viewHolder.getSummary().setVisibility(View.GONE);
		viewHolder.getName().setText(action.getType() + " - " + fullDateStr);

		String summary = "";
		if (action.getType().equalsIgnoreCase("Water"))
		{
			StringBuilder waterStr = new StringBuilder();

			if (action.getPh() != null)
			{
				waterStr.append("<b>pH: </b>");
				waterStr.append(action.getPh());
				waterStr.append(", ");
			}

			if (action.getRunoff() != null)
			{
				waterStr.append("<b>runoff pH: </b>");
				waterStr.append(action.getRunoff());
				waterStr.append(", ");
			}

			summary += waterStr.toString().length() > 0 ? waterStr.toString().substring(0, waterStr.length() - 2) + "<br/>" : "";

			waterStr = new StringBuilder();

			if (action.getPpm() != null)
			{
				waterStr.append("<b>PPM: </b>");
				waterStr.append(action.getPpm());
				waterStr.append(", ");
			}

			if (action.getAmount() != null)
			{
				waterStr.append("<b>Amount: </b>");
				waterStr.append(action.getAmount());
				waterStr.append("ml/l, ");
			}

			if (action.getTemp() != null)
			{
				waterStr.append("<b>Temp: </b>");
				waterStr.append(action.getTemp());
				waterStr.append("ºC, ");
			}

			summary += waterStr.toString().length() > 0 ? waterStr.toString().substring(0, waterStr.length() - 2) + "<br/>" : "";

			waterStr = new StringBuilder();

			if (action.getAdditives().size() > 0)
			{
				waterStr.append("<b>Additives:</b>");

				for (Additive additive : action.getAdditives())
				{
					if (additive == null || additive.getAmount() == null) continue;

					double converted = additive.getAmount();
					String amountStr = converted == Math.floor(additive.getAmount()) ? String.valueOf((int)converted) : String.valueOf(converted);

					waterStr.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;• ");
					waterStr.append(additive.getDescription());
					waterStr.append("  -  ");
					waterStr.append(amountStr);
					waterStr.append("ml");
					waterStr.append("/");
					waterStr.append("l");
				}
			}

			summary += waterStr.toString();
		}
		else if (action.getType().equalsIgnoreCase("StageChange") && action.getNewStage() != null)
		{
			summary = "Changed to stage " + action.getNewStage().getPrintString();
		}
		else
		{
			if (action.getAction() != null)
			{
				summary = action.getAction().getPrintString();
			}
		}

		if (!TextUtils.isEmpty(action.getNotes()))
		{
			summary += summary.length() > 0 ? "<br/><br/>" : "";
			summary += action.getNotes();
		}

		if (summary.endsWith("<br/>"))
		{
			summary = summary.substring(0, summary.length() - "<br/>".length());
		}

		if (!TextUtils.isEmpty(summary))
		{
			viewHolder.getSummary().setText(Html.fromHtml(summary));
			viewHolder.getSummary().setVisibility(View.VISIBLE);
		}
	}

	@Override public int getItemCount()
	{
		return actions.size();
	}
}
