package com.example.poster.easysellingsellers.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by User on 018 18.08.17.
 */

public class BusProvider {
    private static Bus bus = new Bus();

    public static Bus getInstance() {
        return bus;
    }

    public BusProvider() {
    }
}
