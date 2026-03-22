package com.scouthub;

public class SupabaseConfig {
    // Replace with your actual Supabase URL and Anon Key
    private static final String SUPABASE_URL = "https://your-project-id.supabase.co";
    private static final String SUPABASE_ANON_KEY = "your-anon-key";
    
    public static String getSupabaseUrl() {
        return SUPABASE_URL;
    }
    
    public static String getSupabaseAnonKey() {
        return SUPABASE_ANON_KEY;
    }
}
