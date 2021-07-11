package es.sfernandez.raceyourtrack.garage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import es.sfernandez.raceyourtrack.R;
import es.sfernandez.raceyourtrack.RaceYourTrackApplication;
import model.settings.Settings;
import model.settings.configurable.DrivingModeConfig;
import model.settings.configurable.PedalsConfig;
import model.settings.configurable.SteeringConfig;
import model.settings.configurable.TransmissionConfig;
import utils.viewComponents.CustomSpinner;

public class SettingsFragment extends Fragment {

    //---- Attributes ----
    private Settings settings;

    //---- View Elements ----
    private CustomSpinner<DrivingModeConfig> spinnerDrivingMode;
    private CustomSpinner<PedalsConfig> spinnerPedals;
    private CustomSpinner<TransmissionConfig> spinnerTransmission;
    private CustomSpinner<SteeringConfig> spinnerSteeringWheel;

    //---- Fragment Methods ----
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = Settings.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViewElements();
        addListenersToViewElements();
    }

    @Override
    public void onStop() {
        super.onStop();

        settings.setDrivingModeConfig((DrivingModeConfig)spinnerDrivingMode.getSelectedItem());
        settings.setSteeringConfig((SteeringConfig)spinnerSteeringWheel.getSelectedItem());
        settings.setTransmissionConfig((TransmissionConfig)spinnerTransmission.getSelectedItem());
        settings.setPedalsConfig((PedalsConfig)spinnerPedals.getSelectedItem());

        settings.saveSettings();
        Toast.makeText(getContext(), RaceYourTrackApplication.getContext().getResources().getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();
    }

    //---- Methods ----
    private void initializeViewElements() {
        spinnerDrivingMode = new CustomSpinner<DrivingModeConfig>((AppCompatSpinner) this.getActivity().findViewById(R.id.spinner_driving_mode));
        spinnerDrivingMode.setSelectableValues(DrivingModeConfig.getValues());
        spinnerDrivingMode.setSelection(settings.getDrivingModeConfig(), false);

        spinnerSteeringWheel = new CustomSpinner<SteeringConfig>((AppCompatSpinner) this.getActivity().findViewById(R.id.spinner_steering_wheel));
        spinnerSteeringWheel.setSelectableValues(SteeringConfig.getUserSelectableValues());
        spinnerSteeringWheel.setSelection(settings.getSteeringConfig(), false);

        spinnerTransmission = new CustomSpinner<TransmissionConfig>((AppCompatSpinner) this.getActivity().findViewById(R.id.spinner_transmission));
        spinnerTransmission.setSelectableValues(TransmissionConfig.getUserSelectableValues());
        spinnerTransmission.setSelection(settings.getTransmissionConfig(), false);

        spinnerPedals = new CustomSpinner<PedalsConfig>((AppCompatSpinner) this.getActivity().findViewById(R.id.spinner_pedals));
        spinnerPedals.setSelectableValues(PedalsConfig.getUserSelectableValues());
        spinnerPedals.setSelection(settings.getPedalsConfig(), false);
    }

    private void addListenersToViewElements() {
        spinnerDrivingMode.setOnUserSelectItemListener(() -> {
            DrivingModeConfig config = spinnerDrivingMode.getSelectedItem();
            if(!config.equals(DrivingModeConfig.CUSTOM)) {
                spinnerSteeringWheel.setSelection(config.getSteeringConfig(), false);
                spinnerTransmission.setSelection(config.getTransmissionConfig(), false);
                spinnerPedals.setSelection(config.getPedalsConfig(), false);
            }
        });

        spinnerSteeringWheel.setOnUserSelectItemListener(() -> {
            if(!spinnerDrivingMode.getSelectedItem().equals(DrivingModeConfig.CUSTOM)) {
                spinnerDrivingMode.setSelection(DrivingModeConfig.CUSTOM, false);
            }
        });

        spinnerTransmission.setOnUserSelectItemListener(() -> {
            if(!spinnerDrivingMode.getSelectedItem().equals(DrivingModeConfig.CUSTOM)) {
                spinnerDrivingMode.setSelection(DrivingModeConfig.CUSTOM, false);
            }
        });

        spinnerPedals.setOnUserSelectItemListener(() -> {
            if(!spinnerDrivingMode.getSelectedItem().equals(DrivingModeConfig.CUSTOM)) {
                spinnerDrivingMode.setSelection(DrivingModeConfig.CUSTOM, false);
            }
        });
    }

}