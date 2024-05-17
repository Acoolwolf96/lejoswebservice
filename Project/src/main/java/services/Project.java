package services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import conn.Connections;
import data.LejosData;

@Path("/team19")
public class Project {

	@GET
	@Path("/readall")
	@Produces(MediaType.TEXT_PLAIN)
	public String readAll() {
		StringBuilder response = new StringBuilder();
		String sql = "SELECT leftMotor, rightMotor, securityDistance, lineColor FROM lejos_data";
		try (Connection conn = Connections.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				if (response.length() > 0) {
					response.append(", "); // Separate entries by comma if needed
				}
				// Include all values separated by commas
				response.append(rs.getInt("leftMotor")).append(",").append(rs.getInt("rightMotor")).append(",")
						.append(rs.getInt("securityDistance")).append(",").append(rs.getInt("lineColor"));
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Proper error handling or logging should be added here
			return "Error processing request"; // Returning an error message if there's an exception
		}
		return response.toString();
	}

	@POST
	@Path("/adddata")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addData(@FormParam("leftMotor") int leftMotor, @FormParam("rightMotor") int rightMotor,
			@FormParam("securityDistance") int securityDistance, @FormParam("lineColor") int lineColor) {
		try (Connection conn = Connections.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO lejos_data (leftMotor, rightMotor, securityDistance, lineColor) VALUES (?, ?, ?, ?)")) {
			stmt.setInt(1, leftMotor);
			stmt.setInt(2, rightMotor);
			stmt.setInt(3, securityDistance);
			stmt.setInt(4, lineColor);
			int result = stmt.executeUpdate();
			if (result > 0) {
				return Response.ok("Data added successfully").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("No data added").build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/lejosdata/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public LejosData getLejosData(@PathParam("id") int id) {
		String sql = "SELECT * FROM lejos_data order by id desc";
		LejosData data = null;
		try (Connection conn = Connections.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				data = new LejosData(rs.getInt("leftMotor"), rs.getInt("rightMotor"), rs.getInt("securityDistance"),
						rs.getInt("lineColor"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@POST
	@Path("/sendData")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response receiveData(@FormParam("leftMotor") int leftMotor, @FormParam("rightMotor") int rightMotor,
			@FormParam("securityDistance") int securityDistance, @FormParam("lineColor") int lineColor) {
		String sql = "INSERT INTO robot_data (leftMotor, rightMotor, securityDistance, lineColor) VALUES (?, ?, ?, ?)";

		try (Connection conn = Connections.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, leftMotor);
			stmt.setInt(2, rightMotor);
			stmt.setInt(3, securityDistance);
			stmt.setInt(4, lineColor);
			int result = stmt.executeUpdate();
			if (result > 0) {
				return Response.ok("Data added successfully").build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity("No data added").build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage())
					.build();
		}
	}

	@GET
	@Path("/latestData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchLatestData() {
		String sql = "SELECT leftMotor, rightMotor, securityDistance, lineColor FROM robot_data ORDER BY id DESC LIMIT 1";
		try (Connection conn = Connections.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				JsonObject data = Json.createObjectBuilder().add("leftMotor", rs.getInt("leftMotor"))
						.add("rightMotor", rs.getInt("rightMotor"))
						.add("securityDistance", rs.getInt("securityDistance")).add("lineColor", rs.getInt("lineColor"))
						.build();
				return Response.ok(data.toString()).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Database error: " + e.getMessage())
					.build();
		}
		return Response.status(Response.Status.NOT_FOUND).entity("No data available").build();
	}
}
