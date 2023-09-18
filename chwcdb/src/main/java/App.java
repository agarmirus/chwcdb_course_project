import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;

public class App
{
    static protected String readConfigFile(String fileName) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String result = "";

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null)
        {
            sb.append(line);
            sb.append(System.lineSeparator());

            line = br.readLine();
        }

        result = sb.toString();

        br.close();

        return result;
    }

    static public void main(String[] args)
    {
        try
        {
            String configString = readConfigFile("config.json");
            var configJSONObject = new JSONObject(configString);

            String connString = configJSONObject.getString("conn");
            String logLevelString = configJSONObject.getString("logLevel");
            String redisConnString = configJSONObject.getString("redisConn");
            String influxConnString = configJSONObject.getString("influxConn");
            
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
