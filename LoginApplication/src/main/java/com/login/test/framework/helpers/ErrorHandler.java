/*
 * Copyright (c) 2019 The Emirates Group.
 * All Rights Reserved.
 * 
 * The information specified here is confidential and remains property of the Emirates Group.
 */
package com.login.test.framework.helpers;

import org.junit.Assert;

/**
 * The class ErrorHandler - Singleton class to assert the error.
 *
 * @author S717259
 */
public final class ErrorHandler {

	/**
	 * Private constructor to avoid object Instantiation
	 */
	private ErrorHandler() {
	};

	/**
	 * Assert the Error.
	 *
	 * @param message   the message
	 * @param exception the exception
	 */
	public static void assertError(String message, Exception exception) {
		Assert.assertFalse(message + exception, true);
	}

	public static void assertError(String message) {
		Assert.assertFalse(message, true);
	}
}
