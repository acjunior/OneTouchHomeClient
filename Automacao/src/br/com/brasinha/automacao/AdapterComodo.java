package br.com.brasinha.automacao;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AdapterComodo extends BaseExpandableListAdapter {

	private Activity context;
	private Map<String, List<String>> listaItem;
	private List<String> listaComodo;
	
	public AdapterComodo (Activity context, List<String> listaComodo, Map<String, List<String>> listaItem) {
		this.context = context;
		this.listaComodo = listaComodo;
		this.listaItem = listaItem;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Stub de método gerado automaticamente
		return listaItem.get(listaComodo.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Stub de método gerado automaticamente
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean arg2, View view, ViewGroup arg4) {
		// TODO Stub de método gerado automaticamente
		
		final String item = (String) getChild(groupPosition, childPosition);
		LayoutInflater inflater =  context.getLayoutInflater();
		
		if(view == null) {
			view = inflater.inflate(R.layout.item_comodo, null);
		}
		
		TextView itemTextView = (TextView)view.findViewById(R.id.itemComodoTextView);
		itemTextView.setText(item);
		
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Stub de método gerado automaticamente
		return listaItem.get(listaComodo.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Stub de método gerado automaticamente
		return listaComodo.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Stub de método gerado automaticamente
		return listaComodo.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Stub de método gerado automaticamente
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean childPosition, View view, ViewGroup arg3) {
		// TODO Stub de método gerado automaticamente
		
		String comodo = (String) getGroup(groupPosition);
		
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.titulo_comodo, null);
		}
		
		TextView comodoTextView = (TextView) view.findViewById(R.id.nomeComodoTextView);
		comodoTextView.setText(comodo);
		
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Stub de método gerado automaticamente
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Stub de método gerado automaticamente
		return true;
	}
}
