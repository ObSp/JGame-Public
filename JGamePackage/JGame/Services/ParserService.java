package JGamePackage.JGame.Services;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import JGamePackage.JGame.JGame;
import JGamePackage.JGame.Instances.*;
import JGamePackage.JGame.Types.Vector2;
import JGamePackage.lib.ArrayDictionary;
import JGamePackage.lib.JSONSimple.*;
import JGamePackage.lib.JSONSimple.parser.JSONParser;

public class ParserService extends Service {

    private ArrayDictionary<String, Object> classes = new ArrayDictionary<>(new String[] {
        "Box2D", "Image2D", "Oval2D"
    }, new Object[] {Box2D.class, Image2D.class, Oval2D.class});
    
    

    public ParserService(JGame parent){
        super(parent);
    }



    @SuppressWarnings("unchecked")
    private JSONObject Vector2ToJSONObject(Vector2 v){
        JSONObject obj = new JSONObject();
        obj.put("X", v.X);
        obj.put("Y", v.Y);
        return obj;
    }

    private Vector2 JSONObjectToVector2(JSONObject obj){
        return new Vector2((double) obj.get("X"), (double) obj.get("Y"));
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ArrayList<Instance> ParseJSONToInstances(File file){
        ArrayList<Instance> instances = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);

            JSONArray instArr = (JSONArray) parser.parse(reader);

            Iterator<JSONObject> iter = instArr.iterator();

            while (iter.hasNext()){
                JSONObject obj = iter.next();

                String objClass = (String) obj.get("ClassName");

                Class c = (Class) classes.get(objClass);

                Instance inst = (Instance) c.getConstructor().newInstance();

                inst.CFrame.Position = JSONObjectToVector2((JSONObject) obj.get("Position"));
                inst.CFrame.Rotation = (double) obj.get("Rotation");
                inst.Size = JSONObjectToVector2((JSONObject) obj.get("Size"));
                inst.Velocity = JSONObjectToVector2((JSONObject) obj.get("Velocity"));
                inst.AnchorPoint = JSONObjectToVector2((JSONObject) obj.get("AnchorPoint"));

                inst.ZIndex = (int) (((long)obj.get("ZIndex")));

                inst.Solid = (boolean) obj.get("Solid");
                inst.Anchored = (boolean) obj.get("Anchored");
                
                inst.FillColor = new Color((int) (long) obj.get("FillColor"));

                inst.Name = (String) obj.get("Name");

                instances.add(inst);

            }


        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return instances;
    }

    @SuppressWarnings("unchecked")
    public void ExportInstancesToJSON(Instance[] instances, File destination){
        JSONArray arr = new JSONArray();

        for (Instance i : instances){
            JSONObject obj = new JSONObject();

            //CFRAME AND Vector2s
            obj.put("Position", Vector2ToJSONObject(i.CFrame.Position));
            obj.put("Rotation", i.CFrame.Rotation);
            obj.put("Size", Vector2ToJSONObject(i.Size));
            obj.put("Velocity", Vector2ToJSONObject(i.Velocity));
            obj.put("AnchorPoint", Vector2ToJSONObject(i.AnchorPoint));

            //color
            obj.put("FillColor", i.FillColor.getRGB());

            //BOOLEANS
            obj.put("Solid", i.Solid);
            obj.put("MoveWithCamera", i.MoveWithCamera);
            obj.put("Anchored", i.Anchored);

            //ints
            obj.put("ZIndex", i.ZIndex);

            //other
            obj.put("Name", i.Name);
            obj.put("ClassName", i.getClass().getSimpleName());
            
            arr.add(obj);
        }

        try (FileWriter writer = new FileWriter(destination)){
            writer.write(arr.toJSONString());
            writer.flush();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    
}