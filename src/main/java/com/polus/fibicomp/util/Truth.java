package com.polus.fibicomp.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public enum Truth {

	TRUE(new String[] { "true", "yes", "Y", "on", "1", "t", "enabled" }), FALSE(
			new String[] { "false", "no", "N", "off", "0", "f", "disabled" });

	private final Collection<String> truthStrings;

	private Truth(String[] vals) {
		this.truthStrings = Collections.unmodifiableCollection(Arrays.asList(vals));
	}

	public Collection<String> getTruthStrings() {
		return this.truthStrings;
	}

	public static Boolean strToBooleanIgnoreCase(String str) {
		return strToBooleanIgnoreCase(str, (Boolean) null);
	}

	public static Boolean strToBooleanIgnoreCase(String str, Boolean defaultValue) {
		if (str == null) {
			return defaultValue;
		} else {
			Iterator<String> arg1 = TRUE.getTruthStrings().iterator();

			String s;
			do {
				if (!arg1.hasNext()) {
					arg1 = FALSE.getTruthStrings().iterator();

					do {
						if (!arg1.hasNext()) {
							return defaultValue;
						}

						s = (String) arg1.next();
					} while (!s.equalsIgnoreCase(str));

					return Boolean.FALSE;
				}

				s = (String) arg1.next();
			} while (!s.equalsIgnoreCase(str));

			return Boolean.TRUE;
		}
	}

}
