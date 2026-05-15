package com.alien.api.hive2.policy;

@FunctionalInterface
public interface HivePolicyModifier {
    void modify(HivePolicyContext context, MutableHivePolicy policy);
}
