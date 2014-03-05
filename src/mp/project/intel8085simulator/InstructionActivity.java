package mp.project.intel8085simulator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class InstructionActivity extends ListActivity {
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(InstructionActivity.this, android.R.layout.simple_list_item_1, Map.str3));
		
				
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Intent res = new Intent();
		res.putExtra("hexcode", Map.str2[position]);
		res.putExtra("instruction", Map.str1[position]);
		setResult(RESULT_OK, res);
		finish();
	}


	
}
