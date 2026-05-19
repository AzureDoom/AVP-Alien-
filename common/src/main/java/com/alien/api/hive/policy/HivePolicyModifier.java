package com.alien.api.hive.policy;

@FunctionalInterface
public interface HivePolicyModifier {

    void modify(HivePolicyContext context, MutableHivePolicy policy);
}
