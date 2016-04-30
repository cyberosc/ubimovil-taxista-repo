package co.com.acktos.ubimoviltaxista.models;

import co.com.acktos.ubimoviltaxista.app.Config;

/**
 * Created by Acktos on 4/27/16.
 */
public class State {

    private String id;
    private String name;
    private int iconResource;
    private String[] available_states;

    public State(String id, String name, int iconResource, String[] availableStates) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.available_states=availableStates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String[] getAvailable_states() {
        return available_states;
    }

    public void setAvailable_states(String[] available_states) {
        this.available_states = available_states;
    }


}
