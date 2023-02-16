package gamma.engine.core.scene;

public class Component {

	protected transient Entity entity;
	private transient State currentState;

	public final void process(float delta) {
		switch(this.currentState) {
			case NEW -> this.onStart();
			case READY -> this.onUpdate(delta);
		}
		this.currentState = State.READY;
	}

	protected void onStart() {

	}

	protected void onUpdate(float delta) {

	}

	// TODO: On Exit?

	private enum State {
		NEW,
		READY
	}
}
