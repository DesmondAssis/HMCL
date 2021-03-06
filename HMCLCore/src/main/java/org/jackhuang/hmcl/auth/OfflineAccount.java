/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2017  huangyuhui <huanghongxun2008@126.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.auth;

import java.net.Proxy;
import java.util.Map;
import java.util.Objects;
import org.jackhuang.hmcl.util.Lang;
import org.jackhuang.hmcl.util.Pair;
import org.jackhuang.hmcl.util.StringUtils;

/**
 *
 * @author huang
 */
public class OfflineAccount extends Account {

    private final String username;
    private final String uuid;

    OfflineAccount(String username, String uuid) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(uuid);

        this.username = username;
        this.uuid = uuid;

        if (StringUtils.isBlank(username))
            throw new IllegalArgumentException("Username cannot be blank");
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public AuthInfo logIn(Proxy proxy) throws AuthenticationException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(uuid))
            throw new AuthenticationException("Username cannot be empty");

        return new AuthInfo(username, uuid, uuid);
    }

    @Override
    public void logOut() {
        // Offline account need not log out.
    }

    @Override
    public Map<Object, Object> toStorage() {
        return Lang.mapOf(
                new Pair<>("uuid", uuid),
                new Pair<>("username", username)
        );
    }

    @Override
    public String toString() {
        return "OfflineAccount[username=" + username + ", uuid=" + uuid + "]";
    }
}
