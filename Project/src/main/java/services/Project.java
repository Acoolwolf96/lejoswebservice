package services;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
}
