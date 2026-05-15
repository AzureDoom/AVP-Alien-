package com.alien.api.hive2.policy;

public interface MutableHivePolicy {
    <T> T get(HivePolicyKey<T> key);

    <T> void set(HivePolicyKey<T> key, T value);
}
