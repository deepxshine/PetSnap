//
//  SearchService.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 10.11.2024.
//

import Foundation

class SearchController{
    static let shared = SearchController()
    private let searchUrl = ConstantsService.apiUrl + "/user/search"
    
    private init(){print(searchUrl)}
    
    func search(username: String, completion: @escaping(Result<[User], Error>) ->Void){
        print("Searching user: \(username)")
        
        guard let url = URL(string: searchUrl + "?username=\(username)") else {
            print("Invalid Url")
            completion(.failure(NSError(domain: "Invalid Url", code: -1)))
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print("Request url=\(request.url?.absoluteString ?? "NO URL")")
        let task = URLSession.shared.dataTask(with: request){data, response, error in
            if let error = error {
                completion(.failure(error))
                print("Error during request search: \(error.localizedDescription)")
                return
            }
            
            guard let data = data else {
                completion(.failure(NSError(domain: "No data", code: -1, userInfo: nil)))
                print("No data received")
                return
            }
            do {
                let searchResponse = try JSONDecoder().decode([User].self, from: data)
                completion(.success(searchResponse))
                print("Successfully got user \(searchResponse)")
            } catch {
                print("DecoderError: \(error.localizedDescription)")
            }
            
        }
        task.resume()
        print("Request to search user send")
        
    }
    
}
