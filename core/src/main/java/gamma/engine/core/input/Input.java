package gamma.engine.core.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

public final class Input {

	private static final HashMap<String, ArrayList<BooleanSupplier>> ACTIONS = new HashMap<>();

//	static {
//		HashMap<String, ArrayList<HashMap<String, Object>>> input = FileUtils.parseYaml("/input.yaml");
		// TODO: Better loading
//		input.forEach((actionKey, actions) -> {
//			ACTIONS.put(actionKey, new ArrayList<>());
//			actions.forEach(action -> {
//				switch((String) action.get("type")) {
//					case "keyboard" -> ACTIONS.get(actionKey).add(() -> Keyboard.isKeyPressed((int) action.get("key")));
//					case "mouse" -> ACTIONS.get(actionKey).add(() -> Mouse.isButtonPressed((int) action.get("button")));
//				}
//			});
//		});
//	}

	public static boolean isActionPressed(String action) {
		if(ACTIONS.containsKey(action)) {
			return ACTIONS.get(action).stream().anyMatch(BooleanSupplier::getAsBoolean);
		}
		return false;
	}

	public static int getAxis(String positiveAction, String negativeAction) {
		boolean positive = isActionPressed(positiveAction);
		boolean negative = isActionPressed(negativeAction);
		return positive ? (negative ? 0 : 1) : (negative ? -1 : 0);
	}
}
