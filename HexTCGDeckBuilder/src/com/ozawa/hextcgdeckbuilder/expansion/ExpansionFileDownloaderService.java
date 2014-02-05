package com.ozawa.hextcgdeckbuilder.expansion;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class ExpansionFileDownloaderService extends DownloaderService {
	
	// the Base64-encoded RSA public key for your publisher account
	private static final String PUBLIC_KEY = "MIIDsTCCApmgAwIBAgIEL9/crTANBgkqhkiG9w0BAQsFADCBiDEQMA4GA1UEBhMHVW5rbm93bjEQMA4GA1UECBMHVW5rbm93bjEQMA4GA1UEBxMHVW5rbm93bjEsMCoGA1UEChMjVGhyZWUgQ29vbCBHdXlzIERldmVsb3BtZW50IFN0dWRpb3MxEDAOBgNVBAsTB1Vua25vd24xEDAOBgNVBAMTB1Vua25vd24wHhcNMTQwMjA1MTcwNjMyWhcNNDEwNjIzMTcwNjMyWjCBiDEQMA4GA1UEBhMHVW5rbm93bjEQMA4GA1UECBMHVW5rbm93bjEQMA4GA1UEBxMHVW5rbm93bjEsMCoGA1UEChMjVGhyZWUgQ29vbCBHdXlzIERldmVsb3BtZW50IFN0dWRpb3MxEDAOBgNVBAsTB1Vua25vd24xEDAOBgNVBAMTB1Vua25vd24wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCXYkV3q8h7vuRxwu/ydNYxLksdobU0ORWMV0PyyfWyv6mj/FJPa4GaqeOsRdTnn/MKTyg+rnr0nIBQ7cOhUcV/UTVnFo6rbPWXsAqZOUD3Pv3SByHf2YTfnWJ+5AJAsCKPcntUzgdfSWATuXPQuOfuDeZjB3/bITa6d25OzfWOOKYcZu1RojUSeN5Wsd4vLx+JjLf/BcD1gX1sEPQGLOiMVW+7njikBjlITQLVBeYcsmAkyGMcC5s+hwqna72qX9ruFuGnXRFdAyRmDFCVBHcZdB6m7rBDZc9wmFiBNJEOEPaH3RtL7BOk4jqu0iEz3jBaWSESbFNtT4uyer1HlSqXAgMBAAGjITAfMB0GA1UdDgQWBBQ2dt4zS5eXRLCmd4Fudz2eVT2YTjANBgkqhkiG9w0BAQsFAAOCAQEAQosXfthuWHGw0kQy7cLlozmDiGtpKUIhY13Pf9Txf+3UArR2lr6y0MXRMTeXHBkB2skp3tLWPyKZyDmXS+/0ZEAHEkFMXkwPNW1fEuoCLngUeMXkB5KpovIqpTuYVADCuhIjnVwCz25oEuV436rjkYX/zLuzNQ9rYdNO15yDUnc4HBx1Kx5G6+grbZGMeCyMCElTPCrWQ1qjUboPcJoXDGJBEIqwpVdPLlp21cp024K+lLnsXKBH9S13a1k0aw+ERvPOyxLUT5w+7ly7xkmTG1jgaIjS9aK4jrLgnebuFuSIkn6HcO7oGtG6aC85aeHgzz2wAAcx8YdANDu+fCIHMQ==";
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
