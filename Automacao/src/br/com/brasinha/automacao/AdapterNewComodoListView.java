package br.com.brasinha.automacao;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AdapterNewComodoListView extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ItensNewComodo> itens;
	private ArrayAdapter<String> tipoItemArray;
	private String[] tipoItem = {"Lâmpada", "Ventilador"};
	private Context context;
	private ItemSuporte itemHolder;
	
	public AdapterNewComodoListView (Context context, List<ItensNewComodo> itens) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.itens = itens;
	}
	
	/* retorna a lista de itens */
	public List<ItensNewComodo> getList() {
		return itens;
	}
	
	@Override
	public int getCount() {
		// TODO Stub de método gerado automaticamente
		return itens.size();
	}

	@Override
	public ItensNewComodo getItem(int pos) {
		// TODO Stub de método gerado automaticamente
		return itens.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Stub de método gerado automaticamente
		return pos;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Stub de método gerado automaticamente
		
		final ItensNewComodo item = getItem(position);
		
		/* se a view estiver nula (nunca criada), inflamos o layout nela */
		if(view == null) {
			
			/* infla o layout para podermos pegar as views */
			view = mInflater.inflate(R.layout.item_newcomodo, null);
			
			/* cria um item de suporte para não precisarmos sempre inflar as mesmas informações */
			itemHolder = new ItemSuporte();
			itemHolder.nomeItemEditText = ((EditText) view.findViewById(R.id.nomeItemEditText));
			itemHolder.tipoItemTextView = ((TextView) view.findViewById(R.id.tipoTextView));
			itemHolder.numItemTextView = ((TextView) view.findViewById(R.id.numeroTextView));
			itemHolder.numItemEditText = ((EditText) view.findViewById(R.id.numberEditText));
			itemHolder.tipoItemSpinner = ((Spinner) view.findViewById(R.id.tipoSpinner));
			
			tipoItemArray = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tipoItem);
			itemHolder.tipoItemSpinner.setAdapter(tipoItemArray);
			/* define os itens na view */
			view.setTag(itemHolder);
		}
		
		else {
			/* se a view já existe pega os itens */
			itemHolder = (ItemSuporte) view.getTag();
		}
		
		/* evento para salvar as alterações do EditText */
		itemHolder.nomeItemEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Stub de método gerado automaticamente
				if(!hasFocus)
					item.setNomeItem(((EditText)view).getText().toString());
			}
		});
		
		itemHolder.numItemEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Stub de método gerado automaticamente
				if(!hasFocus)
					item.setNumItem(((EditText)view).getText().toString());
			}
		});
		
		itemHolder.tipoItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
				item.setPosItem(adapter.getSelectedItemPosition());
				//Toast.makeText(context, item.getTipoItem(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Stub de método gerado automaticamente
				
			}
		
		});
		
		if(item.getPosItem() == 1)
			itemHolder.tipoItemSpinner.setSelection(1);
		
		else
			itemHolder.tipoItemSpinner.setSelection(0);
		
		itemHolder.nomeItemEditText.setText(item.getNomeItem());
		itemHolder.numItemEditText.setText(item.getNumItem());
		
		return view;
	}
	
	/** classe de suporte para os itens do layout */
	private class ItemSuporte {
		
		EditText nomeItemEditText;
		TextView tipoItemTextView;
		Spinner tipoItemSpinner;
		TextView numItemTextView;
		EditText numItemEditText;
	}
}
