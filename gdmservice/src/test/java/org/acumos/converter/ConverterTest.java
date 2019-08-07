/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */
package org.acumos.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ConverterTest {

	@Test
	public void converterTest() throws ConversionException {

		TestClient testClient = new TestClient();
		TestClient.main(null);

		BooleanToString booleanToString = new BooleanToString();
		booleanToString.convert(false);

		try {
			booleanToString.convert(null);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("BooleanToString Conversion Error : Invalid input");
		}

		FloatToBoolean floatToBoolean = new FloatToBoolean();
		floatToBoolean.convert(1.0F);

		try {
			floatToBoolean.convert(10);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("FloatToBoolean Conversion Error : Invalid input " + 10);
		}

		StringToLong stringToLong = new StringToLong();
		stringToLong.convert("21111");

		try {
			stringToLong.convert("0L");
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("StringToInt Conversion Error : Invalid input " + "0L");
		}

		StringToInt stringToInt = new StringToInt();
		stringToInt.convert("123");

		try {
			stringToInt.convert("test");
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("StringToInt Conversion Error : Invalid input");
		}

		StringToFloat stringToFloat = new StringToFloat();
		stringToFloat.convert("1.0F");

		try {
			stringToFloat.convert("test");
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("StringToFloat Conversion Error : Invalid input");
		}

		LongToInt longToInt = new LongToInt();
		longToInt.convert(1000);
		try {
			longToInt.convert(123L);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("IntToFloat Conversion Error : Invalid input " + 123L);
		}

		IntToLong intToLong = new IntToLong();
		intToLong.convert(100L);

		try {

			intToLong.convert(123);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("IntToLong Conversion Error : Invalid input " + 123);
		}

		IntToFloat intToFloat = new IntToFloat();
		intToFloat.convert(123);

		try {

			intToFloat.convert(123L);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("IntToFloat Conversion Error : Invalid input");
		}

		IntToBoolean intToBoolean = new IntToBoolean();
		intToBoolean.convert(1);

		try {

			intToBoolean.convert(false);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("IntToBoolean Conversion Error : Invalid input " + false);
		}

		FloatToLong floatToLong = new FloatToLong();
		floatToLong.convert(1.0F);

		try {

			floatToLong.convert(1.0);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("FloatToInt Conversion Error : Invalid input " + 1.0);
		}

		FloatToInt floatToInt = new FloatToInt();
		floatToInt.convert(1.0F);

		try {

			floatToInt.convert(1.0);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("FloatToInt Conversion Error : Invalid input");
		}

		FloatToFloat floatToFloat = new FloatToFloat();
		floatToFloat.convert(1.0F);

		try {

			floatToFloat.convert(1.0);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("FloatToFloat Conversion Error : Invalid input");
		}

		BooleanToInt booleanToInt = new BooleanToInt();
		booleanToInt.convert(false);

		try {

			booleanToInt.convert(1);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("BooleanToInt Conversion Error : Invalid input");
		}

		BooleanToFloat booleanToFloat = new BooleanToFloat();
		booleanToFloat.convert(false);

		try {

			booleanToFloat.convert(1);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("BooleanToFloat Conversion Error : Invalid input");
		}

		BooleanToBoolean booleanToBoolean = new BooleanToBoolean();
		booleanToBoolean.convert(false);

		try {

			booleanToBoolean.convert(1);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("BooleanToBoolean Conversion Error : Invalid input");
		}

		LongToFloat longToFloat = new LongToFloat();
		longToFloat.convert(444.33f);
		try {
			longToFloat.convert(1);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(ConversionException.class)
					.hasMessage("IntToFloat Conversion Error : Invalid input " + 1);
		}

	}

}
