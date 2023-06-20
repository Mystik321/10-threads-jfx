package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

public class Cook implements Runnable {

	private String name;

	private ProgressReporter progressReporter;

	private KitchenHatch kitchenHatch;

	public Cook(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
	}

	@Override
	public void run() {
		
		if (kitchenHatch.getOrderCount() != 0) {
			do {

				Order order = kitchenHatch.dequeueOrder(2000);
				Dish dish = new Dish(order.getMealName());

				try {
					Thread.sleep(dish.getCookingTime());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				kitchenHatch.enqueueDish(dish);
				progressReporter.updateProgress();
			} while (kitchenHatch.getOrderCount() != 0);
		}

		progressReporter.notifyCookLeaving();
	}
}
