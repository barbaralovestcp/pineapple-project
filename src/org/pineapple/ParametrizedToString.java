package org.pineapple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ParametrizedToString {
	
	@NotNull
	String toString(@Nullable String parameter);
	@NotNull
	String toString();
}
