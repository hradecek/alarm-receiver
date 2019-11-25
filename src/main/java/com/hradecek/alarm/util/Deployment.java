package com.hradecek.alarm.util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * Represent single verticle deployment with optional {@link DeploymentOptions}.
 */
public class Deployment {

    private final Class<? extends AbstractVerticle> verticleClass;
    private final DeploymentOptions deploymentOptions;

    public static Deployment create(final Class<? extends AbstractVerticle> verticleClass) {
        return new Deployment(verticleClass, new DeploymentOptions());
    }

    public static Deployment create(final Class<? extends AbstractVerticle> verticleClass,
                             final DeploymentOptions deploymentOptions) {
        return new Deployment(verticleClass, deploymentOptions);
    }

    private Deployment(final Class<? extends AbstractVerticle> verticleClass,
                       final DeploymentOptions deploymentOptions) {
        this.verticleClass = verticleClass;
        this.deploymentOptions = deploymentOptions;
    }

    public String getDeploymentName() {
        return verticleClass.getCanonicalName();
    }

    public DeploymentOptions getDeploymentOptions() {
        return deploymentOptions;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getDeploymentName(), deploymentOptions.toJson());
    }
}
