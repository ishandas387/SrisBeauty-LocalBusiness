package com.srisbeauty.srisbeauty.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.srisbeauty.srisbeauty.R;

import java.util.List;

/**
 * Created by ishan on 21-12-2017.
 */

public class MyDailogueFragment extends DialogFragment {


    public MyDailogueFragment() {
        // Required empty public constructor
    }
    LinearLayout linearLayout;
    ListView lv;
    TextView address,addrmarker;
    String [] listItems;
    String addr;

    public void setListItems(List<String> list)
    {
        listItems =list.toArray(new String[list.size()]);
    }

    public void setAddress(String address)
    {
            addr = address;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_viewdetails);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        linearLayout = (LinearLayout) dialog.findViewById(R.id.agree);
        lv = (ListView) dialog.findViewById(R.id.listOfitems);
        address = (TextView) dialog.findViewById(R.id.address);
        addrmarker= (TextView) dialog.findViewById(R.id.addrmarker);
        if(addr != null && !addr.isEmpty())
        {
            address.setText(addr);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 0)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#E0E0E0"));
                }
                return view;
            }
        };

        lv.setAdapter(adapter);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return dialog;
    }

    public void setAddrViewToGone() {
        addrmarker.setVisibility(View.GONE);

    }
}
