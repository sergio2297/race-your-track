package es.sfernandez.raceyourtrack.app_error_handling;

import android.content.Intent;
import android.widget.Toast;

import es.sfernandez.raceyourtrack.MainActivity;
import es.sfernandez.raceyourtrack.R;

/**
 * AppErrorHandler will control every error happened in the app. The actions to take could be show a
 * Toast msg, navigate to a specified activity or recovery the System.
 */
public class AppErrorHandler {

    //---- Definitions and Constants ----
    // Codes: [0,4] --> Errors than must not happen never, and if they happen is caused by an src bug
    private static final int MIN_FATAL_ERROR_CODE = 0, MAX_FATAL_ERROR_CODE = 4;

    // Codes: [5, 9] --> Errors related with Bluetooth connection
    private static final int MIN_BLUETOOTH_ERROR_CODE = MAX_FATAL_ERROR_CODE + 1, MAX_BLUETOOTH_ERROR_CODE = MIN_BLUETOOTH_ERROR_CODE + 4;

    // Codes: [10,14] --> Errors which are related with user inputs
    private static final int MIN_INPUT_ERROR_CODE = MAX_BLUETOOTH_ERROR_CODE + 1, MAX_INPUT_ERROR_CODE = MIN_INPUT_ERROR_CODE + 4;

    // Codes: [15,24] --> Errors which are related with raceway building
    private static final int MIN_RACEWAY_ERROR_CODE = MAX_INPUT_ERROR_CODE + 1, MAX_RACEWAY_ERROR_CODE = MIN_RACEWAY_ERROR_CODE + 9;

    private static final int MAX_ERROR_CODE = 200;

    //---- Handlers ----
    protected static void handleError(AppError error) {
        int errorCode = error.getCode();
        if(errorCode < MIN_FATAL_ERROR_CODE || errorCode > MAX_ERROR_CODE) { // The error has been created without a valid code
            new AppError(CodeErrors.MUST_NOT_HAPPEN_SRC_ERROR_CREATION, error.getContext().getResources().getString(R.string.src_error), error.getContext());
        } else if(MIN_FATAL_ERROR_CODE <= errorCode && errorCode <= MAX_FATAL_ERROR_CODE) { // Errors than never should happen
            Intent intent = new Intent(error.getContext(), FatalAppErrorActivity.class);
            intent.putExtra("ERROR_MSG",error.toString());
            error.getContext().startActivity(intent);
        } else if(MIN_BLUETOOTH_ERROR_CODE <= errorCode && errorCode <= MAX_BLUETOOTH_ERROR_CODE) { // Bluetooth's errors
            Intent intent = new Intent(error.getContext(), MainActivity.class);
            intent.putExtra("ERROR_MSG", error.getMsg());
            error.getContext().startActivity(intent);
        } else if(MIN_INPUT_ERROR_CODE <= errorCode && errorCode <= MAX_INPUT_ERROR_CODE) { // User input's errors
            Toast.makeText(error.getContext(), error.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    //---- Error's codes ----
    public static class CodeErrors {
        public static final int MUST_NOT_HAPPEN = MIN_FATAL_ERROR_CODE + 0;      // Failure that never should happen, and if it happens is caused by a src bug.
        public static final int MUST_NOT_HAPPEN_SRC_ERROR_CREATION = MIN_FATAL_ERROR_CODE + 1;   // Bug in src during an error creation
        public static final int MUST_NOT_HAPPEN_LAP_COUNTER_NOT_INITIALIZED = MIN_FATAL_ERROR_CODE + 2; // Try to start a lap counter without initialized it

        public static final int BLUETOOTH_CONNECTION_LOST = MIN_BLUETOOTH_ERROR_CODE + 0; // Bluetooth connection lost


        public static final int RACEWAY_BUILDING_SOMETHING_NULL = MIN_RACEWAY_ERROR_CODE + 0; // After create a Raceway object, it has some null attribute
        public static final int RACEWAY_BUILDING_INCORRECT_CELLS_COUNT = MIN_RACEWAY_ERROR_CODE + 1; //After create a Raceway object, the number of cells is incorrect
        public static final int RACEWAY_BUILDING_CELLS_SOMETHING_NULL = MIN_RACEWAY_ERROR_CODE + 2; //After create a Raceway object, some cell has some null attribute
        public static final int RACEWAY_BUILDING_CELLS_BAD_ORDERS = MIN_RACEWAY_ERROR_CODE + 3; // After create a Raceway object, the cell's order isn't correct (repeated values, out of bounds...)
        public static final int RACEWAY_BUILDING_INCORRECT_CELLS_LAYOUT = MIN_RACEWAY_ERROR_CODE + 4; //After create a Raceway object, the cells matrix doesn't verify the correct layout
        public static final int RACEWAY_BUILDING_INCORRECT_PIECES_COUNT = MIN_RACEWAY_ERROR_CODE + 5; //After create a Raceway object, the number of pieces is incorrect
        public static final int RACEWAY_BUILDING_PIECES_SOMETHING_NULL = MIN_RACEWAY_ERROR_CODE + 6; //After create a Raceway object, some piece has some null attribute
        public static final int RACEWAY_BUILDING_INCORRECT_PIECES_LAYOUT = MIN_RACEWAY_ERROR_CODE + 7; //After create a Raceway object, the pieces matrix of some cell doesn't verify the correct layout
        public static final int RACEWAY_BUILDING_INCORRECT_NUMBER_OF_SPECIAL_PIECES = MIN_RACEWAY_ERROR_CODE + 8; //After create a Raceway object, number of special checks incorrect
//        public static final int EMPTY_NAME_INPUT = MIN_INPUT_ERROR_CODE + 0; // Se ha dejado el nombre a introducir en blanco
//        public static final int EMPTY_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 1; // Se ha dejado la cantidad objetivo a introducir en blanco
//        public static final int INVALID_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 2; // La cantidad objetivo introducido es <= 0
//        public static final int EMPTY_TARGET_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 3; // Se ha dejado la cantidad objetivo a introducir en blanco
//        public static final int EMPTY_CURRENT_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 4; // Se ha dejado cantidad actual a introducir en blanco
//        public static final int INVALID_TARGET_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 5; // La cantidad objetivo introducido es <= 0
//        public static final int INVALID_CURRENT_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 6; // La cantidad objetivo introducida es <= 0 || > targetAmount
//        public static final int NOT_A_NUMBER_TARGET_AMOUNT_INPUT = MIN_INPUT_ERROR_CODE + 7; // Se ha introducido un numero no valido (Decimales, no un numero...)

    }
}
