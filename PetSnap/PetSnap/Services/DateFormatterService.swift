//
//  Utility.swift
//  PetSnap
//
//  Created by Алексей Евдокимов on 07.12.2024.
//

import Foundation

class DateFormatterService {
    static let shared = DateFormatterService()
    
    private lazy var isoFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        formatter.locale = Locale(identifier: "en_US_POSIX")
        return formatter
    }()
    
    private lazy var readableFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "ru_RU")
        formatter.dateFormat = "dd MMMM HH:mm"
        return formatter
    }()
    
    private lazy var todayTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter
    }()
    
    func formatPostDate(_ dateString: String) -> String {
        guard let date = isoFormatter.date(from: dateString) else {
            return dateString
        }
        
        let calendar = Calendar.current
        
        if calendar.isDateInToday(date) {
            return "Сегодня, \(todayTimeFormatter.string(from: date))"
        } else if calendar.isDateInYesterday(date) {
            return "Вчера, \(todayTimeFormatter.string(from: date))"
        } else {
            return readableFormatter.string(from: date)
        }
    }
}
