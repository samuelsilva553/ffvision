package com.ffvision;

import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_REQ = 1001;
    private static final int MEDIA_REQ = 1002;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.overlay);

        Button btn = findViewById(R.id.btn_start);
        btn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), OVERLAY_REQ);
            } else {
                // Pede permissão para capturar a tela (Essencial para a IA)
                MediaProjectionManager mpm = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mpm.createScreenCaptureIntent(), MEDIA_REQ);
            }
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent d) {
        super.onActivityResult(req, res, d);
        if (req == OVERLAY_REQ && Settings.canDrawOverlays(this)) {
            MediaProjectionManager mpm = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mpm.createScreenCaptureIntent(), MEDIA_REQ);
        } else if (req == MEDIA_REQ && res == RESULT_OK) {
            // Inicia o serviço de captura e o overlay com os dados do MediaProjection
            Intent svc = new Intent(this, ScreenCaptureService.class)
                .putExtra("code", res).putExtra("data", d);
            startForegroundService(svc);
            
            Intent ov = new Intent(this, OverlayService.class);
            startForegroundService(ov);
            finish();
        }
    }
}
