package com.swieghelm.redgnu.config.discovery;

import com.google.common.base.Verify;
import com.swieghelm.redgnu.config.inject.ConfigDescription;

import java.util.Objects;

import static com.google.common.base.Verify.verifyNotNull;

public class ConfigInjection {

    private final InjectionCandidate injectionCandidate;
    private final ConfigDescription description;

    public ConfigInjection(final InjectionCandidate injectionCandidate,
                           final ConfigDescription description) {
        this.injectionCandidate = verifyNotNull(injectionCandidate, "InjectionCandidate must be nonnull");
        this.description = verifyNotNull(description, "Description must be nonnull");
    }

    public InjectionCandidate getInjectionCandidate() {
        return injectionCandidate;
    }

    public ConfigDescription getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof ConfigInjection && isEqual((ConfigInjection) obj));
    }

    private boolean isEqual(final ConfigInjection obj) {
        return Objects.equals(injectionCandidate, obj.injectionCandidate) &&
                Objects.equals(description, obj.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(injectionCandidate, description);
    }

    @Override
    public String toString() {
        return "ConfigInjection [injectionCandidate=" + injectionCandidate + ", description=" + description + "]";
    }
}
