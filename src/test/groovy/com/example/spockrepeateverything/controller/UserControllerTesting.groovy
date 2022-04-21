package com.example.spockrepeateverything.controller

import com.example.spockrepeateverything.exception.NotFoundException
import com.example.spockrepeateverything.model.dto.UserResponseDto
import com.example.spockrepeateverything.service.UserService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

class UserControllerTesting extends Specification {


    private UserController controller
    private UserService service
    private MockMvc mockMvc

    void setup() {
        service = Mock()
        controller = new UserController(service)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandler()).build()
    }


    def "GetUserById"() {
        given:
        def id = 1L
        def dto = UserResponseDto.builder()
                .username("Test")
                .name("Test")
                .lastname("Test")
                .build();

        def endpoint = "/v1/users/" + id
        def expectedResponse = ''' 
                                                    {   
                                                    "name" : "Test",
                                                    "lastname" : "Test",
                                                    "username" : "Test"
                                                    }
                                                    '''

        when:
        var result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        1 * service.getUserById(id) >> dto
        def response = result.response
        response.getStatus() == 200
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false)
    }

    def "GetAll"() {
        given:
        def dto = UserResponseDto.builder()
                .username("hey")
                .name("Victory")
                .lastname("Hey")
                .build()
        def endpoint = "/v1/users"
        def expectedResponse = '''
                                                [{
                                                "name" : "Victory",
                                                "lastname" : "Hey",
                                                "username" :"hey"
                                                
                                                }]
'''
        when:
        var result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)).andReturn()
        then:
        1 * service.getAllUsers() >> List.of(dto)
        def response = result.response
        response.getStatus() == 200
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false)
    }

    def "getUserById error case"() {
        given:
        def id = 1L
        def dto = UserResponseDto.builder()
                .username("Test")
                .name("Test")
                .lastname("Test")
                .build();

        def endpoint = "/v1/users/" + id
        def expectedResponse = ''' 
                                                {   
                                                "message" : "User with id 1 not found",
                                                "code" : "USER_NOT_FOUND"
                                                }
                                                 '''

        when:
        var result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .contentType(MediaType.APPLICATION_JSON)).andReturn()

        then:
        1 * service.getUserById(id) >> {
            throw new NotFoundException("User with id 1 not found", "USER_NOT_FOUND")
        }
        def response = result.response
        response.getStatus() == 404
        JSONAssert.assertEquals(expectedResponse, response.getContentAsString(), false)
    }


    def "get all users error case"() {
        given:
        def dto = UserResponseDto
                .builder()
                .name("a")
                .lastname("a")
                .username("a")
                .build()
        def endpoint = "/v1/users"
        def expectedResponse = """
                                            {
                                            "message" :"Users not found",
                                            "code" : "USERS_NOT_FOUND_EXCEPTION"
                                             }
"""

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        then:
        1 * service.getAllUsers() >> {
            throw new NotFoundException("Users not found", "USERS_NOT_FOUND_EXCEPTION")
        }
    }

}
