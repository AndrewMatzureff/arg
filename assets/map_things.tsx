<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.12.1" name="map_things" tilewidth="32" tileheight="32" tilecount="1" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="3">
  <image source="player_spawn.png" width="32" height="32"/>
  <objectgroup draworder="index" id="2">
   <object id="1" type="RigidBody" x="11.9987" y="7" width="8" height="16">
    <properties>
     <property name="density" type="float" value="100"/>
     <property name="friction" type="float" value="0.25"/>
     <property name="restitution" type="float" value="0.25"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>
