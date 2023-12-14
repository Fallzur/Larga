package com.example.larga;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import com.example.larga.R;

// OverlayHandler.java
public class OverlayHandler {
    private Context context;

    public OverlayHandler(Context context) {
        this.context = context;
    }

    public void showOverlay() {
        final Dialog overlayDialog = new Dialog(context);
        overlayDialog.setContentView(R.layout.overlay_layout);

        Button dismissButton = overlayDialog.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlayDialog.dismiss();
            }
        });

        overlayDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        overlayDialog.setCanceledOnTouchOutside(true);

        overlayDialog.show();
    }
}
