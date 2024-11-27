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
    
    func saveCredentials(token: String, userId: Int) -> Bool{
        let tokenData = token.data(using: .utf8)!
        let userIdData = String(userId).data(using: .utf8)!
        
        
        let tokenQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "authToken",
            kSecValueData as String: tokenData
        ]
        let tokenStatus = SecItemAdd(tokenQuery as CFDictionary, nil)
        
        let userIdQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "userId",
            kSecValueData as String: userIdData
        ]
        
        let userIdStatus = SecItemAdd(userIdQuery as CFDictionary, nil)
        
        return (tokenStatus == errSecSuccess && userIdStatus == errSecSuccess)
    }
    
    func getCredentials() -> (token: String?, userId: Int?) {
        let tokenQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "authToken",
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        
        var tokenResult: AnyObject?
        let tokenStatus = SecItemCopyMatching(tokenQuery as CFDictionary, &tokenResult)
        
        var token: String? = nil
        if tokenStatus == errSecSuccess, let tokenData = tokenResult as? Data {
            token = String(data: tokenData, encoding: .utf8)
        }
        
        let userIdQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "userId",
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        var userIdResult: AnyObject?
        let userIdStatus = SecItemCopyMatching(userIdQuery as CFDictionary, &userIdResult)
        var userId: Int? = nil
        if userIdStatus == errSecSuccess, let userIdData = userIdResult as? Data {
            if let idString = String(data: userIdData, encoding: .utf8){
                userId = Int(idString)
            }
        }
        
        return (token, userId)
    }
            
     func deleteCredentials() -> Bool{
        let tokenQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccount as String: "authToken"
        ]
         let userIdQuery: [String: Any] = [
             kSecClass as String: kSecClassGenericPassword,
             kSecAttrAccount as String: "userId"
         ]
         
         let tokenStatus = SecItemDelete(tokenQuery as CFDictionary)
         let userIdStatus = SecItemDelete(userIdQuery as CFDictionary)
         
         return (tokenStatus == errSecSuccess || tokenStatus == errSecItemNotFound) &&
                 (userIdStatus == errSecSuccess || userIdStatus == errSecItemNotFound)
         
    }
}
