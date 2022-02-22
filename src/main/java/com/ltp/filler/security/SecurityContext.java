package com.ltp.filler.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;

import com.ltp.filler.context.view.BuilderView;
import com.ltp.filler.context.view.FillerView;
import com.ltp.filler.context.view.MenuView;

public class SecurityContext {

    private final Map<Role, List<String>> accesses;
    private Role role;

    private enum Role {
        MANAGER, ADMIN, USER
    }

    public SecurityContext() {

        List<String> adminAccess = List.of(BuilderView.class.getName(),
                FillerView.class.getName(),
                MenuView.class.getName());
        List<String> managerAccess = List.of(BuilderView.class.getName(),
                MenuView.class.getName());
        List<String> userAccess = List.of(FillerView.class.getName(),
                MenuView.class.getName());

        accesses = new HashMap<>();
        accesses.put(Role.USER, userAccess);
        accesses.put(Role.ADMIN, adminAccess);
        accesses.put(Role.MANAGER, managerAccess);
    }

    public boolean login(){
        String login = JOptionPane.showInputDialog("Enter login");
        if(Arrays.stream(Role.values())
                .filter(r -> r.name().equals(login.toUpperCase(Locale.ROOT)))
                .findAny()
                .isEmpty()){
            JOptionPane.showMessageDialog(null,
                    "Invalid username",
                    "Login error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String password = JOptionPane.showInputDialog("Enter password");
        if(!new StringBuilder(password)
                .reverse()
                .toString()
                .toUpperCase(Locale.ROOT)
                .equals(login.toUpperCase(Locale.ROOT))){
            JOptionPane.showMessageDialog(null,
                    "Invalid password",
                    "Login error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        role = Role.valueOf(login.toUpperCase(Locale.ROOT));
        return true;
    }

    public boolean hasAccess(Class<?> clazz) {
        return accesses.get(role).contains(clazz.getName());
    }

}
