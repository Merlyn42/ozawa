/*******************************************************************************
 * Hex TCG Deck Builder
 *     Copyright ( C ) 2014  Chad Kinsella, Dave Kerr and Laurence Reading
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.ozawa.hextcgdeckbuilder.expansion;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class ExpansionFileDownloaderService extends DownloaderService {
	
	// the Base64-encoded RSA public key for your publisher account
	//private static final String PUBLIC_KEY = "MIIDsTCCApmgAwIBAgIEL9/crTANBgkqhkiG9w0BAQsFADCBiDEQMA4GA1UEBhMHVW5rbm93bjEQMA4GA1UECBMHVW5rbm93bjEQMA4GA1UEBxMHVW5rbm93bjEsMCoGA1UEChMjVGhyZWUgQ29vbCBHdXlzIERldmVsb3BtZW50IFN0dWRpb3MxEDAOBgNVBAsTB1Vua25vd24xEDAOBgNVBAMTB1Vua25vd24wHhcNMTQwMjA1MTcwNjMyWhcNNDEwNjIzMTcwNjMyWjCBiDEQMA4GA1UEBhMHVW5rbm93bjEQMA4GA1UECBMHVW5rbm93bjEQMA4GA1UEBxMHVW5rbm93bjEsMCoGA1UEChMjVGhyZWUgQ29vbCBHdXlzIERldmVsb3BtZW50IFN0dWRpb3MxEDAOBgNVBAsTB1Vua25vd24xEDAOBgNVBAMTB1Vua25vd24wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCXYkV3q8h7vuRxwu/ydNYxLksdobU0ORWMV0PyyfWyv6mj/FJPa4GaqeOsRdTnn/MKTyg+rnr0nIBQ7cOhUcV/UTVnFo6rbPWXsAqZOUD3Pv3SByHf2YTfnWJ+5AJAsCKPcntUzgdfSWATuXPQuOfuDeZjB3/bITa6d25OzfWOOKYcZu1RojUSeN5Wsd4vLx+JjLf/BcD1gX1sEPQGLOiMVW+7njikBjlITQLVBeYcsmAkyGMcC5s+hwqna72qX9ruFuGnXRFdAyRmDFCVBHcZdB6m7rBDZc9wmFiBNJEOEPaH3RtL7BOk4jqu0iEz3jBaWSESbFNtT4uyer1HlSqXAgMBAAGjITAfMB0GA1UdDgQWBBQ2dt4zS5eXRLCmd4Fudz2eVT2YTjANBgkqhkiG9w0BAQsFAAOCAQEAQosXfthuWHGw0kQy7cLlozmDiGtpKUIhY13Pf9Txf+3UArR2lr6y0MXRMTeXHBkB2skp3tLWPyKZyDmXS+/0ZEAHEkFMXkwPNW1fEuoCLngUeMXkB5KpovIqpTuYVADCuhIjnVwCz25oEuV436rjkYX/zLuzNQ9rYdNO15yDUnc4HBx1Kx5G6+grbZGMeCyMCElTPCrWQ1qjUboPcJoXDGJBEIqwpVdPLlp21cp024K+lLnsXKBH9S13a1k0aw+ERvPOyxLUT5w+7ly7xkmTG1jgaIjS9aK4jrLgnebuFuSIkn6HcO7oGtG6aC85aeHgzz2wAAcx8YdANDu+fCIHMQ==";
	private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq0syfulL09qLRp/OkOFM/z7xLYo9G87jDBqsW1PAS1IdA+Py95dai/ASW7K7bB+TrkAIPHU5oRj5xUxau7TCzLyaJoRZx9FqBLqi0ct0GRVSNhWNhavg7zYQdXp/KQXf7Jhs2IC6K20/5nhgAVKN6PM1/r6hYHhwNNzmI3a+iwBXTazzDZBfN0y141hTkdc99s5/cB49gwKvezr0YhFjJL86SuwDNWwbKmfEod2akJAtPvbpW6VSSjKwiywlg7Z4mENEhVDJyBuUs0Bm9w1pplnwdegshiJzn+Z1oBLbANrOJytYQYPdJT+AHcbVor/nwA9CWfo8Gn7S8JYcyRkUbwIDAQAB";
	// Generate 20 random bytes, and put them here.
	private static final byte[] SALT = new byte[] {1, 69, -12, -1, 54, 18,
        -100, -32, 33, 2, -8, -4, 9, 5, -106, -107, -33, -75, -1, 84};
	
	@Override public String getPublicKey() {
		return PUBLIC_KEY;
	}
	
	@Override public byte[] getSALT() {
		return SALT;
	}
	
	@Override public String getAlarmReceiverClassName() {
		return ExpansionFileAlarmReceiver.class.getName();
	}
}
