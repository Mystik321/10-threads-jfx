package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch {

	private final int maxDishes;

	private final Deque<Dish> dishes;

	private final Deque<Order> orders;

	public KitchenHatchImpl(int maxMeals, Deque<Order> orders) {
		this.maxDishes = maxMeals;
		this.orders = orders;
		dishes = new LinkedList<>();
	}

	@Override
	public int getMaxDishes() {
		return maxDishes;
	}

	@Override
	public Order dequeueOrder(long timeout) {

		synchronized (orders) {
			if (orders.size() > 0)
				return orders.pop();
			else
				return null;
		}
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout) {

		synchronized (dishes) {
			while (dishes.size() == 0) {
				try {
					dishes.wait(timeout);
				} catch (InterruptedException e) {
					System.out.println("Error when waiting for new dishes: " + e);
				}
			}

			dishes.notifyAll();
			return dishes.pop();
		}
	}

	@Override
	public void enqueueDish(Dish m) {

		synchronized (dishes) {
			while (dishes.size() >= getMaxDishes()) {
				try {
					dishes.wait();
				} catch (InterruptedException e) {
					System.out.println("Error while waiting for enough place to place meal: " + e);
				}
			}

			dishes.push(m);
			dishes.notifyAll();
		}
	}

	@Override
	public synchronized int getDishesCount() {
		return dishes.size();
	}
}
