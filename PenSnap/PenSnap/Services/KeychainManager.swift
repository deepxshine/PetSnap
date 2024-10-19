//
//  KeychainManager.swift
//  PenSnap
//
//  Created by Алексей Евдокимов on 19.10.2024.
//

import Security
import Foundation

class KeychainManager {
    static let shared = KeychainManager()
    
    private init(){}
    
    func saveToken(_ token: String){
        let tokenData = token.data(using: .utf8)!
        
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "authToken",
            kSecValueData as String: tokenData
        ]
        SecItemAdd(query as CFDictionary, nil)
    }
    
    func getToken() -> String? {
            let query: [String: Any] = [
                kSecClass as String: kSecClassGenericPassword,
                kSecAttrAccount as String: "authToken",
                kSecReturnData as String: true,
                kSecMatchLimit as String: kSecMatchLimitOne
            ]
            
            var result: AnyObject?
            SecItemCopyMatching(query as CFDictionary, &result)
            
            if let tokenData = result as? Data {
                return String(data: tokenData, encoding: .utf8)
            }
            
            return nil
        }
    
    func deleteToken(){
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "authToken"
        ]
        SecItemDelete(query as CFDictionary)
    }
}
