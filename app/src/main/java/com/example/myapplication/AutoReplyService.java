public class AutoReplyService extends Service {

    private SmsReceiver smsReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialise et enregistre le récepteur de SMS
        smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Permet au service de se recréer s'il est détruit par le système
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Désenregistre le récepteur de SMS lors de la destruction du service
        unregisterReceiver(smsReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Service non lié
    }
}
