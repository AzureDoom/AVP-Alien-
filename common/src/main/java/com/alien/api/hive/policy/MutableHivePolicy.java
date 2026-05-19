package com.alien.api.hive.policy;

public interface MutableHivePolicy {

    <T> T get(HivePolicyKey<T> key);

    <T> void set(HivePolicyKey<T> key, T value);
}
