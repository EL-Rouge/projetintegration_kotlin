package com.example.projet_integration.network

object FakeData {
    val usersList = """
        [
            {"id": 1, "username": "user1", "password": "pass1", "email": "user1@example.com", "accounttype": "client"},
            {"id": 2, "username": "user2", "password": "pass2", "email": "user2@example.com", "accounttype": "client"},
            {"id": 3, "username": "user3", "password": "pass3", "email": "user3@example.com", "accounttype": "freelancer"},
            {"id": 4, "username": "user4", "password": "pass4", "email": "user4@example.com", "accounttype": "freelancer"}
        ]
    """

    val singleUser = """
        {"id": 1, "username": "user1", "password": "pass1", "email": "user1@example.com", "accounttype": "client"}
    """


    val serviceRequestList = """
        [
            {"requestId": 1, "clientId": 1, "freelancerId": 101, "description": "Fix my computer", "status": "Pending", "paymentStatus": "Unpaid"},
            {"requestId": 2, "clientId": 2, "freelancerId": 102, "description": "Design a website", "status": "In Progress", "paymentStatus": "Paid"}
        ]
    """

    val singleServiceRequest = """
        {"requestId": 1, "clientId": 1, "freelancerId": 101, "description": "Fix my computer", "status": "Pending", "paymentStatus": "Unpaid"}
    """
}
