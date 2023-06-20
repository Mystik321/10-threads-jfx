package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;

import java.util.Random;

public class Waiter implements Runnable {

	private String name;

	private ProgressReporter progressReporter;

	private KitchenHatch kitchenHatch;

	private final Random random = new Random();

	public Waiter(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
	}

	@Override
	public void run() {

		while (kitchenHatch.getOrderCount() > 0 || kitchenHatch.getDishesCount() != 0) {

			kitchenHatch.dequeueDish();
			try {
				Thread.sleep(random.nextInt(10000));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			progressReporter.updateProgress();
		}

		progressReporter.notifyWaiterLeaving();
	}
}
